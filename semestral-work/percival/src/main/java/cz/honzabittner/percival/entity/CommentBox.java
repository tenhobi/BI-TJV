/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.honzabittner.percival.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HoBi
 */
public class CommentBox {
    private List<CommentEntity> comments = new ArrayList();

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }
}
