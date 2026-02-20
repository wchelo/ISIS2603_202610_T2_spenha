package co.edu.uniandes.dse.TallerPruebas.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.TallerPruebas.entities.AccountEntity;
import co.edu.uniandes.dse.TallerPruebas.entities.PocketEntity;
import co.edu.uniandes.dse.TallerPruebas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.TallerPruebas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.TallerPruebas.repositories.AccountRepository;
import co.edu.uniandes.dse.TallerPruebas.repositories.PocketRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PocketRepository pocketRepository;

    @Transactional
    public void addCashToPocket(Long accountId, long pocketId, double monto) throws EntityNotFoundException, BusinessLogicException {
        log.info("Inicia proceso de añadir monto {} a un bolsillo para la cuenta con id = {}", monto, accountId);
        
        // 1. Verificar que la cuenta existe
        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        if (accountEntity.isEmpty()) {
            throw new EntityNotFoundException("La cuenta no existe");
        }

        // 1. Verificar que el bolsillo existe
        Optional<PocketEntity> pocketEntity = pocketRepository.findById(pocketId);
        if (pocketEntity.isEmpty()) {
            throw new EntityNotFoundException("El bolsillo no existe");
        }


        // 3. Verificar que saldoCuenta>= monto
        double saldoCuenta = accountEntity.get().getSaldo();
        if (saldoCuenta < monto) {
            throw new BusinessLogicException("Saldo insuficiente");
        }

        // 4. Actualizar el saldo del bolsillo y de la cuenta
        accountEntity.get().setSaldo(saldoCuenta - monto);
        pocketEntity.get().setSaldo(pocketEntity.get().getSaldo() + monto);
        accountRepository.save(accountEntity.get());
        pocketRepository.save(pocketEntity.get());
    }

    

}
