/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.honzabittner.percival.entity;

import java.util.List;

/**
 *
 * @author HoBi
 */
public class ArticleBox {
    private List<ArticleEntity> articles;

    public List<ArticleEntity> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleEntity> articles) {
        this.articles = articles;
    }
}
