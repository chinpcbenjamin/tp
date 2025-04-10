package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MATRIC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EVENTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STAFF;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalExternalParties.JESSICA;
import static seedu.address.testutil.TypicalStaffs.HARIS;
import static seedu.address.testutil.TypicalStudents.JAMAL;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.model.event.Event;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.person.ExternalParty;
import seedu.address.model.person.Staff;
import seedu.address.model.person.Student;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.ExternalPartyBuilder;
import seedu.address.testutil.StaffBuilder;
import seedu.address.testutil.StudentBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasStaff_nullStaff_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasStaff(null));
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasStudent(null));
    }

    @Test
    public void hasExternalParty_nullExternalParty_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasExternalParty(null));
    }

    @Test
    public void hasStaff_staffNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasStaff(new StaffBuilder().build()));
    }

    @Test
    public void hasStaff_staffInAddressBook_returnsTrue() {
        Staff staff = new StaffBuilder().build();
        modelManager.addStaff(staff);
        assertTrue(modelManager.hasStaff(staff));
    }

    @Test
    public void hasExternalParty_externalPartyNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasExternalParty(new ExternalPartyBuilder().build()));
    }

    @Test
    public void hasExternalParty_externalPartyInAddressBook_returnsTrue() {
        ExternalParty externalParty = new ExternalPartyBuilder().build();
        modelManager.addExternalParty(externalParty);
        assertTrue(modelManager.hasExternalParty(externalParty));
    }

    @Test
    public void hasStudent_studentNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasStudent(new StudentBuilder().build()));
    }

    @Test
    public void hasStudent_studentInAddressBook_returnsTrue() {
        Student student = new StudentBuilder().build();
        modelManager.addStudent(student);
        assertTrue(modelManager.hasStudent(student));
    }

    @Test
    public void deleteStaff_nullStaff_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.deleteStaff(null));
    }

    @Test
    public void deleteStaff_staffDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> modelManager.deleteStaff(new StaffBuilder().build()));
    }

    @Test
    public void deleteStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.deleteStudent(null));
    }

    @Test
    public void deleteStudent_studentDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> modelManager.deleteStudent(new StudentBuilder().build()));
    }

    @Test
    public void setStaff_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setStaff(null, new StaffBuilder().build()));
    }

    @Test
    public void setStaff_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setStaff(new StaffBuilder().build(), null));
    }

    @Test
    public void setStaff_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> modelManager.setStaff(
                new StaffBuilder().build(), new StaffBuilder().build()));
    }

    @Test
    public void setStaff_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        Staff staff = new StaffBuilder().build();
        Staff staff1 = new StaffBuilder()
                .withPhone("22222222")
                .withEmail("bob@gmail.com")
                .build();
        modelManager.addStaff(staff);
        modelManager.addStaff(staff1);
        assertThrows(DuplicatePersonException.class, () -> modelManager.setStaff(staff, staff1));
    }

    @Test
    public void setStudent_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setStudent(null,
                new StudentBuilder().build()));
    }

    @Test
    public void setStudent_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setStudent(new StudentBuilder().build(),
                null));
    }

    @Test
    public void setStudent_targetStudentNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> modelManager.setStudent(
                new StudentBuilder().build(), new StudentBuilder().build()));
    }

    @Test
    public void setStudent_editedStudentHasNonUniqueIdentity_throwsDuplicatePersonException() {
        Student student = new StudentBuilder().build();
        Student student1 = new StudentBuilder().withMatric(VALID_MATRIC_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).build();
        modelManager.addStudent(student);
        modelManager.addStudent(student1);
        assertThrows(DuplicatePersonException.class, () -> modelManager.setStudent(student, student1));
    }

    @Test
    public void getFilteredStaffList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredStaffList().remove(0));
    }

    @Test
    public void getFilteredExternalPartyList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                     -> modelManager.getFilteredExternalPartyList().remove(0));
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredStudentList().remove(0));
    }

    @Test
    public void getFilteredEventsList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredEventList().remove(0));
    }

    @Test
    public void updateFilteredStaffList_changesListTypeToStaff() {
        modelManager.setListType(ListType.EVENT);
        assertNotEquals(modelManager.getListType(), ListType.STAFF);
        assertEquals(modelManager.getListType(), ListType.EVENT);

        modelManager.updateFilteredStaffList(PREDICATE_SHOW_ALL_STAFF);
        assertEquals(modelManager.getListType(), ListType.STAFF);
    }

    @Test
    public void updateFilteredStudentList_changesListTypeToStudent() {
        modelManager.setListType(ListType.STAFF);
        assertNotEquals(modelManager.getListType(), ListType.EVENT);
        assertEquals(modelManager.getListType(), ListType.STAFF);

        modelManager.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        assertEquals(modelManager.getListType(), ListType.EVENT);
    }

    @Test
    public void updateFilteredStudentList_changesListTypeToStaff() {
        modelManager.setListType(ListType.EVENT);
        assertNotEquals(modelManager.getListType(), ListType.STUDENT);
        assertEquals(modelManager.getListType(), ListType.EVENT);

        modelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        assertEquals(modelManager.getListType(), ListType.STUDENT);
    }

    @Test
    public void getListType_modify_success() {
        assertEquals(modelManager.getListType(), ListType.STUDENT);
        assertEquals(modelManager.getListTypeProperty().get(), ListType.STUDENT);
        modelManager.setListType(ListType.STAFF);
        assertEquals(modelManager.getListType(), ListType.STAFF);
        assertEquals(modelManager.getListTypeProperty().get(), ListType.STAFF);
        modelManager.setListType(ListType.EXTERNAL);
        assertEquals(modelManager.getListType(), ListType.EXTERNAL);
        assertEquals(modelManager.getListTypeProperty().get(), ListType.EXTERNAL);
        modelManager.setListType(ListType.EVENT);
        assertEquals(modelManager.getListType(), ListType.EVENT);
        assertEquals(modelManager.getListTypeProperty().get(), ListType.EVENT);
        modelManager.setListType(ListType.STUDENT);
        assertEquals(modelManager.getListType(), ListType.STUDENT);
        assertEquals(modelManager.getListTypeProperty().get(), ListType.STUDENT);
    }

    @Test
    public void deleteEvent_eventNotInAddressBook_throwsException() {
        Event event = new EventBuilder().build();
        assertThrows(EventNotFoundException.class, () -> modelManager.deleteEvent(event));
    }

    @Test
    public void hasEvent_eventNotInAddressBook_returnsFalse() {
        Event event = new EventBuilder().build();
        assertFalse(modelManager.hasEvent(event));
    }

    @Test
    public void hasEvent_eventInAddressBook_returnsTrue() {
        Event event = new EventBuilder().build();
        modelManager.addEvent(event);
        assertTrue(modelManager.hasEvent(event));
    }

    @Test
    public void setSelectedEventDetail_setsCorrectEventAndIndex() {
        Event event = new EventBuilder().build();
        Index index = Index.fromZeroBased(0);
        modelManager.addEvent(event);

        modelManager.setSelectedEventDetail(event, index);
        assertEquals(event, modelManager.getSelectedEventDetail());
        assertEquals(index, modelManager.getSelectedEventIndex());
    }


    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder()
                .withStaff(HARIS).withStudent(JAMAL).withExternalParty(JESSICA).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }


}
