/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.honzabittner.lancelot.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HoBi
 */
public class UserBox {
    private List<UserEntity> users = new ArrayList();

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
