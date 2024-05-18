package Service;

import DAO.AccountDao;
import Dtos.AccountDto;
import Model.Account;

public class AccountService {
    
    private AccountDao accountDao;

    public AccountService() {
        this.accountDao = new AccountDao();
    }


    public Account registerAccount(AccountDto accountDto){
        return  this.accountDao.registerAccount(accountDto);
    }

    public boolean accountExists(String username){
        return this.accountDao.accountExists(username);
    }

    public boolean accountExists(int id){
        return this.accountDao.accountExists(id);
    }

    public Account login(AccountDto accountDto){
        Account dbAccount = this.accountDao.getAccountByUsername(accountDto.getUsername());
        if(dbAccount != null && dbAccount.getPassword().equals(accountDto.getPassword())){
            return dbAccount;
        }else{
            return null;
        }
    }
}
