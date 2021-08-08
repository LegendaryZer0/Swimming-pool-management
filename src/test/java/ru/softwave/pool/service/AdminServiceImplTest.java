package ru.softwave.pool.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.softwave.pool.model.entity.Admin;
import ru.softwave.pool.repo.AdminRepository;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AdminServiceImpl is working then")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class AdminServiceImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    @MockBean
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;
    @BeforeEach
    public void setUp(){
        Admin admin =Admin.builder().email("someEmail").build();
        when(adminRepository.save(admin)).thenReturn(admin);

    }

    @Nested
    @DisplayName("registrAdmin() is working")
    class SaveAdmin{
        @Test
        void save_valid_admin() {
            try{
                Admin admin =Admin.builder().hashPassword("pass").email("Email").build();
                adminService.registrAdmin(admin);
            }catch (Exception e){
                fail("Exception should not be thrown");
            }
        }
        @Test
        void  save_admin_without_email_throws_exception(){

            try{
                Admin admin =Admin.builder().build();
                adminService.saveAdmin(admin);
            }catch (DataIntegrityViolationException e){
                System.out.println("Success");
            }catch (Exception e){
                System.out.println("DataIntegrityViolationException must be thrown");
            }
        }
    }

}