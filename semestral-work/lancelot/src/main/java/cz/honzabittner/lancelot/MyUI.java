package cz.honzabittner.lancelot;

import cz.honzabittner.lancelot.clienti.UserClient;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import cz.honzabittner.lancelot.entity.UserBox;
import cz.honzabittner.lancelot.entity.UserEntity;

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
        
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        /*
        Window subWindow = new Window("Sub-window");
        VerticalLayout subContent = new VerticalLayout();
        subWindow.setContent(subContent);

        subContent.addComponent(new Label("Meatball sub"));
        subContent.addComponent(new Button("Awlright"));

        subWindow.center();
        */
        //addWindow(subWindow);
        
        Button button = new Button("Click Me");
        layout.addComponent(button);
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
        });
        
        UserClient uc = new UserClient();
        UserBox users = uc.findAllUsers_JSON(UserBox.class);
        
        users.getUsers().get(0).getComments().forEach((comment) -> {
          layout.addComponent(new Label(comment.getId().toString()));
        });
        
        //Grid<UserEntity> grid = new Grid<>();
        //grid.setItems(users.getUsers());
        //grid.setCaption("ah");
        //grid.addColumn("name", String.class);
        //grid.setSelectionMode(SelectionMode.NONE);
        //layout.addComponent(grid);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
