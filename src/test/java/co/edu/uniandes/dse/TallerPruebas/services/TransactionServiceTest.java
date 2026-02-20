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
@Import(TransactionService.class)
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AccountEntity> accountList = new ArrayList<>();

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

    }

    @Test
    void testMoveCashBetweenAccounts() throws EntityNotFoundException, BusinessLogicException {
        accountList.get(0).setSaldo(2000.0);
        accountList.get(1).setSaldo(1000.0);
        Double saldoA = accountList.get(0).getSaldo();
        Double saldoB = accountList.get(1).getSaldo();

        long firstId = accountList.get(0).getId();
        long secondId = accountList.get(1).getId();

        transactionService.moveCashBetweenAccounts(firstId, secondId, 1000.0);

        assertEquals(saldoA - 1000.0, accountList.get(0).getSaldo());
        assertEquals(saldoB + 1000.0, accountList.get(1).getSaldo());

    }

    @Test
    void testMoveCashBetweenAccountsWithoutOrigin() {
        long notId = 999L;
        long Id = accountList.get(1).getId();

        assertThrows(EntityNotFoundException.class, () -> {
            transactionService.moveCashBetweenAccounts(notId, Id, 1000.0);
        });
    }

    @Test
    void testMoveCashBetweenAccountsWithoutEnd() {
        long notId = 999L;
        long Id = accountList.get(1).getId();

        assertThrows(EntityNotFoundException.class, () -> {
            transactionService.moveCashBetweenAccounts(Id, notId, 1000.0);
        });
    }

    @Test
    void testMoveCashBetweenAccountsWithSameAccount() {
        long sameId = accountList.get(0).getId();

        assertThrows(BusinessLogicException.class, () -> {
            transactionService.moveCashBetweenAccounts(sameId, sameId, 1000.0);
        });
    }
    @Test
    void testMoveCashBetweenAccountsWithNotEnoughMoney() {
        accountList.get(0).setSaldo(500.0);
        long firstId = accountList.get(0).getId();
        long secondId = accountList.get(1).getId();


        assertThrows(BusinessLogicException.class, () -> {
            transactionService.moveCashBetweenAccounts(firstId, secondId, 1000.0);
        });

    
    }
}
