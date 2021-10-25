package socialite.ui;

import java.io.IOException;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import socialite.model.handle.Handle;
import socialite.model.handle.Handle.Platform;
import socialite.model.person.Person;
import socialite.model.person.Remark;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private HBox handles;
    @FXML
    private HBox facebook;
    @FXML
    private ImageView facebookIcon;
    @FXML
    private Label facebookHandle;
    @FXML
    private HBox instagram;
    @FXML
    private ImageView instagramIcon;
    @FXML
    private Label instagramHandle;
    @FXML
    private HBox telegram;
    @FXML
    private ImageView telegramIcon;
    @FXML
    private Label telegramHandle;
    @FXML
    private HBox tiktok;
    @FXML
    private ImageView tiktokIcon;
    @FXML
    private Label tiktokHandle;
    @FXML
    private HBox twitter;
    @FXML
    private ImageView twitterIcon;
    @FXML
    private Label twitterHandle;
    @FXML
    private FlowPane tags;
    @FXML
    private Label remark;


    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        remark.managedProperty().bind(remark.visibleProperty());
        handles.setSpacing(8);
        this.makeRemark(person.getRemark());
        this.makeHandle(person.getFacebook(), Platform.FACEBOOK);
        this.makeHandle(person.getInstagram(), Platform.INSTAGRAM);
        this.makeHandle(person.getTelegram(), Platform.TELEGRAM);
        this.makeHandle(person.getTiktok(), Platform.TIKTOK);
        this.makeHandle(person.getTwitter(), Platform.TWITTER);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    private void makeRemark(Remark remark) {
        String value = remark.get();
        if (value != null && !value.equals("")) {
            this.remark.setText(value);
            this.remark.setVisible(true);
        } else {
            this.remark.setText(null);
            this.remark.setVisible(false);
        }
    }


    private void makeHandle(Handle handle, Platform platform) {
        HBox box = null;
        Label label = null;
        ImageView icon = null;

        switch (platform) {
        case FACEBOOK:
            box = this.facebook;
            label = this.facebookHandle;
            icon = this.facebookIcon;
            break;
        case INSTAGRAM:
            box = this.instagram;
            label = this.instagramHandle;
            icon = this.instagramIcon;
            break;
        case TELEGRAM:
            box = this.telegram;
            label = this.telegramHandle;
            icon = this.telegramIcon;
            break;
        case TIKTOK:
            box = this.tiktok;
            label = this.tiktokHandle;
            icon = this.tiktokIcon;
            break;
        case TWITTER:
            box = this.twitter;
            label = this.twitterHandle;
            icon = this.twitterIcon;
            break;
        default:
        }

        // if platform is correct, label and icon should not be null
        assert box != null;
        assert label != null;
        assert icon != null;

        box.managedProperty().bind(box.visibleProperty());
        renderHandle(box, handle, label, icon, "/images/" + platform.name().toLowerCase() + ".png");
    }


    private void renderHandle(HBox box, Handle handle, Label label, ImageView icon, String iconFilePath) {
        if (handle.get() != null && !handle.get().equals("")) {
            box.setVisible(true);
            icon.setImage(new Image(this.getClass().getResourceAsStream(iconFilePath)));
            label.setText("@" + handle + " ");
            label.setOnMouseEntered(Event -> label.setUnderline(true));
            label.setOnMouseExited(Event -> label.setUnderline(false));
            label.setOnMouseClicked(Event -> this.openBrowser(handle.getUrl()));
        } else {
            box.setVisible(false);
        }
    }

    private void openBrowser(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();
        try {
            if (os.indexOf("win") >= 0) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.indexOf("mac") >= 0) {
                runtime.exec("open " + url);
            } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
                runtime.exec("xdg-open " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
