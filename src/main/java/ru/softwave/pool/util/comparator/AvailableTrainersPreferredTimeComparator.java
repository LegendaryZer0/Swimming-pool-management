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

  private Time groupTimeStart;
  private Time groupTimeEnd;

  private long groupTimeStartSec;
  private long groupTimeEndSec;

  private boolean groupTimeNightMode;
  private boolean trainerTimeNightMode;

  @Override
  public int compare(AvailableTrainersDto o1, AvailableTrainersDto o2) {
    long o1TimeStartMs = convertTimeToSeconds(o1.getPreferedTimeDto().getStartTime());
    long o1TimeEndMs = convertTimeToSeconds(o1.getPreferedTimeDto().getEndTime());

    long o2TimeStartMs = convertTimeToSeconds(o2.getPreferedTimeDto().getStartTime());
    long o2TimeEndMs = convertTimeToSeconds(o2.getPreferedTimeDto().getEndTime());
    log.trace(
        "time start o1: {} time end o1: {} time start o2: {} time end o2: {}",
        o1TimeStartMs,
        o1TimeEndMs,
        o2TimeStartMs,
        o2TimeEndMs);

    trainerTimeNightMode = o1TimeEndMs < o1TimeStartMs;
    if (groupTimeEndSec < groupTimeStartSec) {
      groupTimeNightMode = true;
    } else {
      trainerTimeNightMode = false;
    }

    long o1resolvedTime =
        resolveTime(o1TimeStartMs, o1TimeEndMs, trainerTimeNightMode, groupTimeNightMode);
    trainerTimeNightMode = o2TimeEndMs < o2TimeStartMs;
    long o2resolvedTime =
        resolveTime(o2TimeStartMs, o2TimeEndMs, trainerTimeNightMode, groupTimeNightMode);
    log.trace(
        "started comparing o1 {}, o2 {} of o1 name {} o2 name {}",
        o1resolvedTime,
        o2resolvedTime,
        o1.getName(),
        o2.getName());

    if (o2resolvedTime > o1resolvedTime) {
      log.trace("i return {}", 1);
      return 1;
    } else if (o2resolvedTime == o1resolvedTime) {
      log.trace("i return {}", 0);
      return 0;
    } else {
      log.trace("i return {}", -1);
      return -1;
    }
  }

  private long resolveTime(
      long preferTimeStart,
      long preferTimeEnd,
      boolean trainerTimeNightMode,
      boolean groupTimeNightMode) {

    if (trainerTimeNightMode && groupTimeNightMode) {

      long minResult =
          Math.min(preferTimeEnd, groupTimeEndSec)
              - Math.max(preferTimeStart, groupTimeStartSec)
              + Time.valueOf("23:59:99").getTime();
      log.trace("i return Math.min result {}", minResult);
      return minResult;
    } else if ((!trainerTimeNightMode && groupTimeNightMode)) { // у группы ночная смена
      if (preferTimeStart > groupTimeEndSec && preferTimeEnd < groupTimeStartSec) {
        log.trace("returning min val");
        return Long.MIN_VALUE;
      } else if (preferTimeStart <= groupTimeEndSec && preferTimeEnd <= groupTimeStartSec) {
        return groupTimeEndSec - preferTimeStart;
      } else if (preferTimeStart >= groupTimeEndSec && preferTimeEnd >= groupTimeStartSec) {
        return preferTimeEnd - groupTimeStartSec;
      } else if (preferTimeStart <= groupTimeEndSec && preferTimeEnd >= groupTimeStartSec) {
        return groupTimeEndSec - preferTimeStart + preferTimeEnd - groupTimeStartSec;
      } else return preferTimeEnd - preferTimeStart;
    } else if ((trainerTimeNightMode && !groupTimeNightMode)) {

      if (groupTimeStartSec > preferTimeEnd && groupTimeEndSec < preferTimeStart) {
        log.trace("return min val");
        return Long.MIN_VALUE;
      } else if (groupTimeStartSec <= preferTimeEnd && groupTimeEndSec <= preferTimeStart) {

        return preferTimeEnd - groupTimeStartSec;
      } else if (groupTimeStartSec >= preferTimeEnd && groupTimeEndSec >= preferTimeStart) {

        return groupTimeEndSec - preferTimeStart;
      } else if (groupTimeStartSec <= preferTimeEnd && groupTimeEndSec >= preferTimeStart) {
        return preferTimeEnd - groupTimeStartSec + groupTimeEndSec - preferTimeStart;
      } else return groupTimeEndSec - groupTimeStartSec;
    } else if (!trainerTimeNightMode && !groupTimeNightMode) {

      if ((preferTimeStart < groupTimeStartSec && preferTimeEnd < groupTimeStartSec)
          || (preferTimeEnd > groupTimeEndSec && preferTimeStart > groupTimeEndSec)) {
        return Long.MIN_VALUE;
      }

      log.trace(
          "they aboth in day mode {}",
          Math.min(preferTimeEnd, groupTimeEndSec) - Math.max(preferTimeStart, groupTimeStartSec));
      log.trace("min time {}", Math.min(preferTimeEnd, groupTimeEndSec));
      log.trace("max time {}", Math.max(preferTimeStart, groupTimeStartSec));
      return Math.min(preferTimeEnd, groupTimeEndSec)
          - Math.max(preferTimeStart, groupTimeStartSec);
    }
    return Long.MIN_VALUE;
  }

  private long convertTimeToSeconds(Time time) {
    LocalTime localTime = time.toLocalTime();
    log.trace(localTime.toString());
    return localTime.getHour() * 3600 + localTime.getMinute() * 60 + localTime.getSecond();
  }

  @Deprecated
  private long resolveTime(long preferTimeStart, long preferTimeEnd) {

    if ((preferTimeStart > groupTimeEndSec && preferTimeEnd < groupTimeStartSec)
        || ((preferTimeStart == groupTimeEndSec) && (preferTimeEnd == groupTimeStartSec))) {
      log.trace("i return min value {}", Long.MIN_VALUE + 1);
      return Long.MIN_VALUE;
    } else if (preferTimeStart > groupTimeStartSec
        && preferTimeEnd > groupTimeStartSec
        && preferTimeStart > preferTimeEnd) {
      log.trace("i return doubled intersect val");
      return preferTimeEnd - groupTimeStartSec + groupTimeEndSec - preferTimeStart;
    } else {
      long minResult =
          Math.min(preferTimeEnd, groupTimeEndSec) - Math.max(preferTimeStart, groupTimeStartSec);
      log.trace("i return Math.min result {}", minResult);
      return minResult;
    }
  }
}
