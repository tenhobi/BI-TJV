package cz.honzabittner.lancelot;

import cz.honzabittner.lancelot.client.UserClient;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import cz.honzabittner.lancelot.client.ArticleClient;
import cz.honzabittner.lancelot.client.CommentClient;
import cz.honzabittner.lancelot.entity.ArticleBox;
import cz.honzabittner.lancelot.entity.ArticleEntity;
import cz.honzabittner.lancelot.entity.CommentBox;
import cz.honzabittner.lancelot.entity.CommentEntity;
import cz.honzabittner.lancelot.entity.UserBox;
import cz.honzabittner.lancelot.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ws.rs.ClientErrorException;
import vaadin.scala.IndexedContainer;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        // Boxes
        HorizontalLayout userBox = new HorizontalLayout();
        userBox.setSizeFull();
        
        HorizontalLayout articleBox = new HorizontalLayout();
        articleBox.setSizeFull();
        articleBox.setVisible(false);
        
        HorizontalLayout commentBox = new HorizontalLayout();
        commentBox.setSizeFull();
        commentBox.setVisible(false);
        
        // Clients
        UserClient uc = new UserClient();
        ArticleClient ac = new ArticleClient();
        CommentClient cc = new CommentClient();
        
        // User Grid
        UserBox users = uc.findAllUsers_JSON(UserBox.class);
        Grid<UserEntity> userGrid = initUserGrid(users.getUsers());
        userBox.addComponent(userGrid);
        userBox.setExpandRatio(userGrid, 0.8f);
        
        // User Form
        FormLayout userForm = new FormLayout();
        TextField utf0 = new TextField("ID");
        utf0.setEnabled(false);
        TextField utf1 = new TextField("Jméno");
        TextField utf2 = new TextField("Přijmení");
        TextField utf3 = new TextField("Přezdívka");
        DateField utf4 = new DateField("Datum narození");
        TextField utf5 = new TextField("Popis");
        Button ub1 = new Button("Editovat");
        Button ub2 = new Button("Smazat");
        Button ub3 = new Button("Vytvořit");
        ub1.addClickListener(e -> {
            UserEntity user = new UserEntity();
            user.setId(Long.parseLong(utf0.getValue()));
            user.setFirstName(utf1.getValue());
            user.setLastName(utf2.getValue());
            user.setNickname(utf3.getValue());
            user.setBirthdate(utf4.getValue());
            user.setDescription(utf5.getValue());
            uc.edit_JSON(user, utf0.getValue());
            UserBox tmp = uc.findAllUsers_JSON(UserBox.class);
            userGrid.setItems(tmp.getUsers());
            userForm.setVisible(false);
        });
        ub2.addClickListener(e -> {
            uc.remove(utf0.getValue());
            UserBox tmp = uc.findAllUsers_JSON(UserBox.class);
            userGrid.setItems(tmp.getUsers());
            userForm.setVisible(false);
        });
        ub3.addClickListener(e -> {
            UserEntity user = new UserEntity();
            user.setFirstName(utf1.getValue());
            user.setLastName(utf2.getValue());
            user.setNickname(utf3.getValue());
            user.setBirthdate(utf4.getValue());
            user.setDescription(utf5.getValue());
            uc.create_JSON(user);
            UserBox tmp = uc.findAllUsers_JSON(UserBox.class);
            userGrid.setItems(tmp.getUsers());
            userForm.setVisible(false);
        });
        userForm.setVisible(false);
        userForm.addComponents(utf0, utf1, utf2, utf3, utf4, utf5, new HorizontalLayout(ub1, ub2, ub3));
        
        userGrid.addSelectionListener(event -> {
            Set<UserEntity> selected = event.getAllSelectedItems();
            
            if (selected.size() > 0) {
                UserEntity user = selected.stream().findFirst().get();
                userForm.setVisible(true);
                ub1.setVisible(true);
                ub2.setVisible(true);
                ub3.setVisible(false);
                
                utf0.setValue(user.getId().toString());
                utf1.setValue(user.getFirstName());
                utf2.setValue(user.getLastName());
                utf3.setValue(user.getNickname());
                utf4.setValue(user.getBirthdate());
                utf5.setValue(user.getDescription());
            } else {
                userForm.setVisible(false);
                ub1.setVisible(false);
                ub2.setVisible(false);
                ub3.setVisible(false);
                
                utf0.setValue("");
                utf1.setValue("");
                utf2.setValue("");
                utf3.setValue("");
                utf4.setValue(null);
                utf5.setValue("");
            }            
        });
        
        userBox.addComponent(userForm);
        userBox.setExpandRatio(userForm, 0.2f);

        // Article Grid
        ArticleBox articles = ac.findAllArticles_JSON(ArticleBox.class);
        Grid<ArticleEntity> articleGrid = initArticleGrid(articles.getArticles());
        articleBox.addComponent(articleGrid);
        articleBox.setExpandRatio(articleGrid, 1.0f);
        
        // Article Form
        FormLayout articleForm = new FormLayout();
        TextField atf0 = new TextField("ID");
        atf0.setEnabled(false);
        TextField atf1 = new TextField("ID autora");
        TextField atf2 = new TextField("Název");
        TextField atf3 = new TextField("Obsah");
        Button ab1 = new Button("Editovat");
        Button ab2 = new Button("Smazat");
        Button ab3 = new Button("Vytvořit");
        ab1.addClickListener(e -> {
            ArticleEntity article = new ArticleEntity();
            article.setId(Long.parseLong(atf0.getValue()));
            UserEntity a = new UserEntity();
            a.setId(Long.parseLong(atf1.getValue()));
            article.setAuthor(a);
            article.setTitle(atf2.getValue());
            article.setContent(atf3.getValue());
            ac.edit_JSON(article, atf0.getValue());
            ArticleBox tmp = ac.findAllArticles_JSON(ArticleBox.class);
            articleGrid.setItems(tmp.getArticles());
            articleForm.setVisible(false);
        });
        ab2.addClickListener(e -> {
            ac.remove(atf0.getValue());
            ArticleBox tmp = ac.findAllArticles_JSON(ArticleBox.class);
            articleGrid.setItems(tmp.getArticles());
            articleForm.setVisible(false);
        });
        ab3.addClickListener(e -> {
            ArticleEntity article = new ArticleEntity();
            UserEntity a = new UserEntity();
            a.setId(Long.parseLong(atf1.getValue()));
            article.setAuthor(a);
            article.setTitle(atf2.getValue());
            article.setContent(atf3.getValue());
            ac.create_JSON(article);
            ArticleBox tmp = ac.findAllArticles_JSON(ArticleBox.class);
            articleGrid.setItems(tmp.getArticles());
            articleForm.setVisible(false);
        });
        articleForm.setVisible(false);
        articleForm.addComponents(atf0, atf1, atf2, atf3, new HorizontalLayout(ab1, ab2, ab3));
        
        articleGrid.addSelectionListener(event -> {
            Set<ArticleEntity> selected = event.getAllSelectedItems();
            
            if (selected.size() > 0) {
                ArticleEntity article = selected.stream().findFirst().get();
                articleForm.setVisible(true);
                ab1.setVisible(true);
                ab2.setVisible(true);
                ab3.setVisible(false);
                
                atf0.setValue(article.getId().toString());
                atf1.setValue((article.getAuthor() == null) ? null : article.getAuthor().getId().toString());
                atf2.setValue(article.getTitle());
                atf3.setValue(article.getContent());
            } else {
                articleForm.setVisible(false);
                ab1.setVisible(false);
                ab2.setVisible(false);
                ab3.setVisible(false);
                
                atf0.setValue("");
                atf1.setValue("");
                atf2.setValue("");
                atf3.setValue("");
            }            
        });
        
        articleBox.addComponent(articleForm);
        articleBox.setExpandRatio(articleForm, 0.2f);
        
        // Comment Grid
        CommentBox comments = cc.findAllComments_JSON(CommentBox.class);
        Grid<CommentEntity> commentGrid = initCommentGrid(comments.getComments());
        commentBox.addComponent(commentGrid);
        commentBox.setExpandRatio(commentGrid, 1.0f);
        
        // Comment Form
        FormLayout commentForm = new FormLayout();
        TextField ctf0 = new TextField("ID");
        ctf0.setEnabled(false);
        TextField ctf1 = new TextField("ID autora");
        TextField ctf2 = new TextField("ID článku");
        TextField ctf3 = new TextField("Komentář");
        Button cb1 = new Button("Editovat");
        Button cb2 = new Button("Smazat");
        Button cb3 = new Button("Vytvořit");
        cb1.addClickListener(e -> {
            CommentEntity comment = new CommentEntity();
            comment.setId(Long.parseLong(ctf0.getValue()));
            UserEntity a = new UserEntity();
            a.setId(Long.parseLong(ctf1.getValue()));
            comment.setAuthor(a);
            ArticleEntity b = new ArticleEntity();
            b.setId(Long.parseLong(ctf2.getValue()));
            comment.setArticle(b);
            comment.setContent(ctf3.getValue());
            cc.edit_JSON(comment, ctf0.getValue());
            CommentBox tmp = cc.findAllComments_JSON(CommentBox.class);
            commentGrid.setItems(tmp.getComments());
            commentForm.setVisible(false);
        });
        cb2.addClickListener(e -> {
            cc.remove(ctf0.getValue());
            CommentBox tmp = cc.findAllComments_JSON(CommentBox.class);
            commentGrid.setItems(tmp.getComments());
            commentForm.setVisible(false);
        });
        cb3.addClickListener(e -> {
            CommentEntity comment = new CommentEntity();
            UserEntity a = new UserEntity();
            a.setId(Long.parseLong(ctf1.getValue()));
            comment.setAuthor(a);
            ArticleEntity b = new ArticleEntity();
            b.setId(Long.parseLong(ctf2.getValue()));
            comment.setArticle(b);
            comment.setContent(ctf3.getValue());
            cc.create_JSON(comment);
            CommentBox tmp = cc.findAllComments_JSON(CommentBox.class);
            commentGrid.setItems(tmp.getComments());
            commentForm.setVisible(false);
        });
        commentForm.setVisible(false);
        commentForm.addComponents(ctf0, ctf1, ctf2, ctf3, new HorizontalLayout(cb1, cb2, cb3));
        
        commentGrid.addSelectionListener(event -> {
            Set<CommentEntity> selected = event.getAllSelectedItems();
            System.out.println(selected.size());
            if (selected.size() > 0) {
                CommentEntity comment = selected.stream().findFirst().get();
                commentForm.setVisible(true);
                cb1.setVisible(true);
                cb2.setVisible(true);
                cb3.setVisible(false);
                
                ctf0.setValue(comment.getId().toString());
                ctf1.setValue((comment.getAuthor() == null) ? null : comment.getAuthor().getId().toString());
                ctf2.setValue((comment.getArticle() == null) ? null : comment.getArticle().getId().toString());
                ctf3.setValue(comment.getContent());
            } else {
                commentForm.setVisible(false);
                cb1.setVisible(false);
                cb2.setVisible(false);
                cb3.setVisible(false);
                
                ctf0.setValue("");
                ctf1.setValue("");
                ctf2.setValue("");
                ctf3.setValue("");
            }            
        });
        
        commentBox.addComponent(commentForm);
        commentBox.setExpandRatio(commentForm, 0.2f);
        
        // Buttons
        Button userButton = new Button("Uživatelé");
        userButton.addClickListener(e -> {
            userBox.setVisible(true);
            articleBox.setVisible(false);
            commentBox.setVisible(false);
        });
        
        Button articleButton = new Button("Články");
        articleButton.addClickListener(e -> {
            userBox.setVisible(false);
            articleBox.setVisible(true);
            commentBox.setVisible(false);
        });
         
        Button commentButton = new Button("Komentáře");
        commentButton.addClickListener(e -> {
            userBox.setVisible(false);
            articleBox.setVisible(false);
            commentBox.setVisible(true);
        });
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(userButton);
        buttons.addComponent(articleButton);
        buttons.addComponent(commentButton);
        
        // Search
        HorizontalLayout search = new HorizontalLayout();
        TextField searchText = new TextField();
        Button searchButton = new Button("Vyhledat");
        searchButton.addClickListener(e -> {
          if (userBox.isVisible()) {
              UserBox tmp = uc.findSearchedUsers_JSON(UserBox.class, searchText.getValue());
              userGrid.setItems(tmp.getUsers());
          } else if (articleBox.isVisible()) {
              ArticleBox tmp = ac.findSearchedArticles_JSON(ArticleBox.class, searchText.getValue());
              articleGrid.setItems(tmp.getArticles());
          } else if (commentBox.isVisible()) {
              CommentBox tmp = cc.findSearchedComments_JSON(CommentBox.class, searchText.getValue());
              commentGrid.setItems(tmp.getComments());
          }
        });
        search.addComponent(searchText);
        search.addComponent(searchButton);
        
        Button newItem = new Button("Vytvořit záznam");
        newItem.addClickListener(e -> {
            if (userBox.isVisible()) {
                userForm.setVisible(true);
                ub1.setVisible(false);
                ub2.setVisible(false);
                ub3.setVisible(true);
                
                utf0.setValue("");
                utf1.setValue("");
                utf2.setValue("");
                utf3.setValue("");
                utf4.setValue(null);
                utf5.setValue("");
            } else if (articleBox.isVisible()) {
                articleForm.setVisible(true);
                ab1.setVisible(false);
                ab2.setVisible(false);
                ab3.setVisible(true);
                
                atf0.setValue("");
                atf1.setValue("");
                atf2.setValue("");
                atf3.setValue("");
            } else if (commentBox.isVisible()) {
                commentForm.setVisible(true);
                cb1.setVisible(false);
                cb2.setVisible(false);
                cb3.setVisible(true);
                
                ctf0.setValue("");
                ctf1.setValue("");
                ctf2.setValue("");
                ctf3.setValue("");
          }
        });
        
        // Layout        
        layout.addComponents(buttons, search, newItem, userBox, articleBox, commentBox);
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
    
    private Grid<UserEntity> initUserGrid(List<UserEntity> users) {
        Grid<UserEntity> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(users);
        grid.addColumn(UserEntity::getId).setCaption("ID");
        grid.addColumn(UserEntity::getFirstName).setCaption("Jméno");
        grid.addColumn(UserEntity::getLastName).setCaption("Příjmení");
        grid.addColumn(UserEntity::getNickname).setCaption("Přezdívka");
        grid.addColumn(UserEntity::getBirthdate).setCaption("Datum narození");
        grid.addColumn(UserEntity::getDescription).setCaption("Popis");
        return grid;
    }
   
    private Grid<ArticleEntity> initArticleGrid(List<ArticleEntity> articles) {
        Grid<ArticleEntity> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(articles);
        grid.addColumn(ArticleEntity::getId).setCaption("ID");
        grid.addColumn((articleEntity) -> {
            if (articleEntity.getAuthor() == null) {
              return null;
            }
            
            return articleEntity.getAuthor().getId();
        }).setCaption("ID autora");
        grid.addColumn(ArticleEntity::getTitle).setCaption("Název");
        grid.addColumn(ArticleEntity::getContent).setCaption("Obsah");
        return grid;
    }
    
    private Grid<CommentEntity> initCommentGrid(List<CommentEntity> comments) {
        Grid<CommentEntity> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(comments);
        grid.addColumn(CommentEntity::getId).setCaption("ID");
        grid.addColumn((commentEntity) -> {
            if (commentEntity.getAuthor() == null) {
              return null;
            }
            return commentEntity.getAuthor().getId();
        }).setCaption("ID autora");
        grid.addColumn((commentEntity) -> {
            if (commentEntity.getArticle()== null) {
              return null;
            }
            return commentEntity.getArticle().getId();
        }).setCaption("ID článku");
        grid.addColumn(CommentEntity::getContent).setCaption("Komentář");
        return grid;
    }
        
    
}
        