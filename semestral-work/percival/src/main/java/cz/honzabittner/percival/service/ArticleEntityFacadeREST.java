/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.honzabittner.percival.service;

import cz.honzabittner.percival.entity.ArticleBox;
import cz.honzabittner.percival.entity.ArticleEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author HoBi
 */
@Stateless
@Path("cz.honzabittner.percival.articleentity")
public class ArticleEntityFacadeREST extends AbstractFacade<ArticleEntity> {

    @PersistenceContext(unitName = "cz.honzabittner_percival_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ArticleEntityFacadeREST() {
        super(ArticleEntity.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ArticleEntity entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, ArticleEntity entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArticleEntity find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArticleBox findAllArticles() {
        ArticleBox articles = new ArticleBox();
        articles.setArticles(super.findAll());
        return articles;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ArticleBox findRangeArticles(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        ArticleBox articles = new ArticleBox();
        articles.setArticles(super.findRange(new int[]{from, to}));
        return articles;
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
