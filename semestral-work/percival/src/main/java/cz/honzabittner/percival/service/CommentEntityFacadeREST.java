/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.honzabittner.percival.service;

import cz.honzabittner.percival.entity.CommentBox;
import cz.honzabittner.percival.entity.CommentEntity;
import java.util.ArrayList;
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
@Path("cz.honzabittner.percival.entity.commententity")
public class CommentEntityFacadeREST extends AbstractFacade<CommentEntity> {

    @PersistenceContext(unitName = "cz.honzabittner_percival_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public CommentEntityFacadeREST() {
        super(CommentEntity.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(CommentEntity entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, CommentEntity entity) {
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
    public CommentEntity find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CommentBox findAllComments() {
        CommentBox comments = new CommentBox();
        comments.setComments(super.findAll());
        return comments;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CommentBox findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        CommentBox comments = new CommentBox();
        comments.setComments(super.findRange(new int[]{from, to}));
        return comments;
    }

    @GET
    @Path("search/{searched}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CommentBox findSearchedComments(@PathParam("searched") String searched) {
        List<CommentEntity> allComments = super.findAll();
        List<CommentEntity> searchedComments = new ArrayList();

        for (CommentEntity comment : allComments) {
            if (comment.getContent() != null && comment.getContent().toLowerCase().contains(searched.trim().toLowerCase())) {
                searchedComments.add(comment);
            }
        }

        CommentBox comments = new CommentBox();
        comments.setComments(searchedComments);
        System.out.println(comments.getComments().size());
        return comments;
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
