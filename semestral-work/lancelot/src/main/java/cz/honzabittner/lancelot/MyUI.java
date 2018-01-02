package cz.honzabittner.lancelot;

import cz.honzabittner.lancelot.client.UserClient;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
        userBox.setExpandRatio(userGrid, 1.0f);
        
        // User Form
        FormLayout userForm = initUserForm();
        userBox.addComponent(userForm);
        
        // Article Grid
        ArticleBox articles = ac.findAllArticles_JSON(ArticleBox.class);
        Grid<ArticleEntity> articleGrid = initArticleGrid(articles.getArticles());
        articleBox.addComponent(articleGrid);
        articleBox.setExpandRatio(articleGrid, 1.0f);
        
        // Comment Grid
        CommentBox comments = cc.findAllComments_JSON(CommentBox.class);
        Grid<CommentEntity> commentGrid = initCommentGrid(comments.getComments());
        commentBox.addComponent(commentGrid);
        commentBox.setExpandRatio(commentGrid, 1.0f);
        
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
        
        // Layout        
        layout.addComponent(buttons);
        layout.addComponent(search);
        layout.addComponent(userBox);        
        layout.addComponent(articleBox);        
        layout.addComponent(commentBox);        
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
    
    private FormLayout initUserForm() {
        FormLayout form = new FormLayout();
        TextField tf1 = new TextField("Name");
        form.addComponent(tf1);

        TextField tf2 = new TextField("Street address");
        form.addComponent(tf2);

        TextField tf3 = new TextField("Postal code");
        form.addComponent(tf3);
        
        return form;
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
        