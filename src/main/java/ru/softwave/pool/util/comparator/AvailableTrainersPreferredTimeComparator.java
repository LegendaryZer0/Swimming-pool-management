package ru.softwave.pool.util.comparator;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.softwave.pool.model.dto.AvailableTrainersDto;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Comparator;

@Slf4j
@Component
@Getter
@Setter
public class AvailableTrainersPreferredTimeComparator implements Comparator<AvailableTrainersDto> {

  private static final String RESULT_OF_COMPARE_LOG_MESSAGE = "i return {}";
  private Time groupTimeStart;
  private Time groupTimeEnd;

  private long groupTimeStartSec;
  private long groupTimeEndSec;

  private boolean groupTimeNightMode;
  private boolean trainerTimeNightMode;

  @Override
  public int compare(AvailableTrainersDto o1, AvailableTrainersDto o2) {
    long o1TimeStartSec = convertTimeToSeconds(o1.getPreferedTimeDto().getStartTime());
    long o1TimeEndMSec = convertTimeToSeconds(o1.getPreferedTimeDto().getEndTime());

    long o2TimeStartSec = convertTimeToSeconds(o2.getPreferedTimeDto().getStartTime());
    long o2TimeEndSec = convertTimeToSeconds(o2.getPreferedTimeDto().getEndTime());
    log.trace(
        "time start o1: {} time end o1: {} time start o2: {} time end o2: {}",
        o1TimeStartSec,
        o1TimeEndMSec,
        o2TimeStartSec,
        o2TimeEndSec);
    // Переход интервала времени через 0:00:00 и есть ночной мод/ночная смена
    trainerTimeNightMode = o1TimeEndMSec < o1TimeStartSec;
    groupTimeNightMode = groupTimeEndSec < groupTimeStartSec;

    long o1resolvedTime =
        resolveTime(o1TimeStartSec, o1TimeEndMSec, trainerTimeNightMode, groupTimeNightMode);
    trainerTimeNightMode = o2TimeEndSec < o2TimeStartSec;
    long o2resolvedTime =
        resolveTime(o2TimeStartSec, o2TimeEndSec, trainerTimeNightMode, groupTimeNightMode);
    log.trace(
        "started comparing o1 {}, o2 {} of o1 email {} o2 e,ail {}",
        o1resolvedTime,
        o2resolvedTime,
        o1.getEmail(),
        o2.getEmail());

    if (o2resolvedTime > o1resolvedTime) {
      log.trace(RESULT_OF_COMPARE_LOG_MESSAGE, 1);
      return 1;
    } else if (o2resolvedTime == o1resolvedTime) {
      log.trace(RESULT_OF_COMPARE_LOG_MESSAGE, 0);
      return 0;
    } else {
      log.trace(RESULT_OF_COMPARE_LOG_MESSAGE, -1);
      return -1;
    }
  }

  private long resolveTime(
      long preferTimeStart,
      long preferTimeEnd,
      boolean trainerTimeNightMode,
      boolean groupTimeNightMode) {

    if (trainerTimeNightMode && groupTimeNightMode) {
      return calculateIntersectionWhenBothInNightMode(preferTimeStart, preferTimeEnd);
    }
    if ((!trainerTimeNightMode && groupTimeNightMode)) {
      return calculateIntersectionTimeWhenOneInNightMode(
          preferTimeStart, preferTimeEnd, groupTimeEndSec, groupTimeStartSec, "returning min val");
    }
    if (trainerTimeNightMode) {
      return calculateIntersectionTimeWhenOneInNightMode(
          groupTimeStartSec, groupTimeEndSec, preferTimeEnd, preferTimeStart, "return min val");
    }

    return calculateIntersectionWhenBothInDayMode(preferTimeStart, preferTimeEnd);
  }

  private long calculateIntersectionWhenBothInNightMode(long preferTimeStart, long preferTimeEnd) {
    log.trace("converted 24 hours in seconds {}  prefer time start {}  prefer time end  {}",convertTimeToSeconds(Time.valueOf("23:59:99")),preferTimeStart,preferTimeEnd);
    long minResult =
        Math.min(preferTimeEnd, groupTimeEndSec)
            - Math.max(preferTimeStart, groupTimeStartSec)
            + convertTimeToSeconds(Time.valueOf("23:59:59"));
    log.trace("they aboth in night mode {}", minResult);
    return minResult;
  }

  private long calculateIntersectionWhenBothInDayMode(long preferTimeStart, long preferTimeEnd) {
    if ((preferTimeStart < groupTimeStartSec && preferTimeEnd < groupTimeStartSec)
        || (preferTimeEnd > groupTimeEndSec && preferTimeStart > groupTimeEndSec)) {
      return Long.MIN_VALUE;
    }
    long intersectionResult =
        Math.min(preferTimeEnd, groupTimeEndSec) - Math.max(preferTimeStart, groupTimeStartSec);
    log.trace("they aboth in day mode {}", intersectionResult);
    log.trace("min time {}", Math.min(preferTimeEnd, groupTimeEndSec));
    log.trace("max time {}", Math.max(preferTimeStart, groupTimeStartSec));
    return intersectionResult;
  }

  private long calculateIntersectionTimeWhenOneInNightMode(
      long dailyTimeStart, long dailyTimeEnd, long nightTimeEnd, long nightTimeStart, String s) {
    if (dailyTimeStart > nightTimeEnd && dailyTimeEnd < nightTimeStart) {
      log.trace(s);
      return Long.MIN_VALUE;
    }
    if (dailyTimeStart <= nightTimeEnd && dailyTimeEnd <= nightTimeStart) {
      return nightTimeEnd - dailyTimeStart;
    }
    if (dailyTimeStart >= nightTimeEnd) {
      return dailyTimeEnd - nightTimeStart;
    }
    return nightTimeEnd - dailyTimeStart + dailyTimeEnd - nightTimeStart;
  }

  private long convertTimeToSeconds(Time time) {
    LocalTime localTime = time.toLocalTime();
    log.trace(localTime.toString());
    return (long) localTime.getHour() * 3600 + localTime.getMinute() * 60 + localTime.getSecond();
  }
}
