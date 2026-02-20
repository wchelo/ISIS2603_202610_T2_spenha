package co.edu.uniandes.dse.TallerPruebas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.TallerPruebas.entities.AccountEntity;
import co.edu.uniandes.dse.TallerPruebas.entities.PocketEntity;
import co.edu.uniandes.dse.TallerPruebas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.TallerPruebas.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(AccountService.class)


public class AccountServiceTest {
    

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AccountEntity> accountList = new ArrayList<>();
    private List<PocketEntity> pocketList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PocketEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AccountEntity").executeUpdate();
    }

    /**
     * Inserta datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AccountEntity accountEntity = factory.manufacturePojo(AccountEntity.class);
            accountEntity.setEstado("ACTIVA");
            entityManager.persist(accountEntity);
            accountList.add(accountEntity);
        }

        for (int i = 0; i < 3; i++) {
            PocketEntity pocketEntity = factory.manufacturePojo(PocketEntity.class);
            pocketEntity.setAccount(accountList.get(0));
            entityManager.persist(pocketEntity);
            pocketList.add(pocketEntity);
        }
        // Actualizar la lista de bolsillos en la cuenta para las validaciones
        accountList.get(0).setPockets(pocketList);
    }

    @Test
    void testAddCashToPocket() throws EntityNotFoundException, BusinessLogicException {
        accountList.get(0).setSaldo(200.0);
        pocketList.get(1).setSaldo(50.0);

        Double saldoA = accountList.get(0).getSaldo();
        Double saldoP = pocketList.get(1).getSaldo();


        accountService.addCashToPocket(accountList.get(0).getId(), pocketList.get(1).getId(), 100.0);
        
        assertEquals(saldoA - 100.0, accountList.get(0).getSaldo());
        assertEquals(saldoP + 100.0, pocketList.get(1).getSaldo());
        
    }

    @Test
    void testAddCashToPocketWithoutAccountId() throws EntityNotFoundException, BusinessLogicException {


         assertThrows(EntityNotFoundException.class, () -> {
        
        
                accountList.get(0).setSaldo(200.0);
                pocketList.get(1).setSaldo(50.0);


                Long idnot= 5667L;

                accountService.addCashToPocket(idnot, pocketList.get(1).getId(), 100.0);

         });
        
    }

        @Test
    void testAddCashToPocketWithoutCash() throws EntityNotFoundException, BusinessLogicException {


         assertThrows(BusinessLogicException.class, () -> {
        
        accountList.get(0).setSaldo(200.0);
        pocketList.get(1).setSaldo(50.0);


        accountService.addCashToPocket(accountList.get(0).getId(), pocketList.get(1).getId(), 500.0);
        

         });
        
    }

    @Test
    void testAddCashToPocketWithoutPocketId() throws EntityNotFoundException, BusinessLogicException {


         assertThrows(EntityNotFoundException.class, () -> {
        
        
                accountList.get(0).setSaldo(200.0);
                pocketList.get(1).setSaldo(50.0);


                Long idnot= 5667L;

                accountService.addCashToPocket(accountList.get(0).getId(), idnot, 100.0);

         });
        }
}
