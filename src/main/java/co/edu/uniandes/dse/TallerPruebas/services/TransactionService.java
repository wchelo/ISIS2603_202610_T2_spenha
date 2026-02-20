package co.edu.uniandes.dse.TallerPruebas.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.TallerPruebas.entities.AccountEntity;
import co.edu.uniandes.dse.TallerPruebas.entities.TransactionEntity;
import co.edu.uniandes.dse.TallerPruebas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.TallerPruebas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.TallerPruebas.repositories.AccountRepository;
import co.edu.uniandes.dse.TallerPruebas.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;  

    @Autowired
    private TransactionRepository transactionRepository;  



    @Transactional
    public TransactionEntity createTransaction(TransactionEntity transactionEntity) throws EntityNotFoundException, BusinessLogicException {
        log.info("Inicia proceso de creación de la transacción");

        if (transactionEntity.getMonto() <= 0) {
            throw new BusinessLogicException("El monto de la transacción debe ser mayor a cero");
        } 
        return transactionRepository.save(transactionEntity);
    }

    @Transactional
    public void moveCashBetweenAccounts(long OriginAccountId, long EndAccountId, double monto) throws EntityNotFoundException, BusinessLogicException {
        log.info("Inicia proceso de movimiento de dinero entre cuentas con id = {} y {}", OriginAccountId, EndAccountId);
        
        // 1. Verificar que la cuenta origen existe
        Optional<AccountEntity> FirstAccountEntity = accountRepository.findById(OriginAccountId);
        if (FirstAccountEntity.isEmpty()) {
            throw new EntityNotFoundException("La cuenta de origen no existe");
        }

        // 2. Verificar que la cuenta destino existe
        Optional<AccountEntity> SecondAccountEntity = accountRepository.findById(EndAccountId);
        if (SecondAccountEntity.isEmpty()) {
            throw new EntityNotFoundException("La cuenta de destino no existe");
        }

        // 3. Verificar que la cuenta origen es distinta a la cuenta destino
        if (OriginAccountId == EndAccountId) {
            throw new BusinessLogicException("La cuenta de origen y destino son iguales");
        }

        // 4. Verificar que la cuenta origen tenga saldo suficiente
        if (FirstAccountEntity.get().getSaldo() < monto) {
            throw new BusinessLogicException("Saldo insuficiente en la cuenta de origen");
        }

        // 5. Actualizar el saldo de las dos cuentas
        FirstAccountEntity.get().setSaldo(FirstAccountEntity.get().getSaldo() - monto);
        SecondAccountEntity.get().setSaldo(SecondAccountEntity.get().getSaldo() + monto);
        accountRepository.save(FirstAccountEntity.get());
        accountRepository.save(SecondAccountEntity.get());
    }


}
