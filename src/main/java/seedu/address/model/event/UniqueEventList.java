package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.person.ExternalParty;
import seedu.address.model.person.Staff;
import seedu.address.model.person.Student;

/**
 * A list that maintains unique events and does not allow duplicates.
 * Supports basic operations like add, remove, and update while ensuring uniqueness.
 */
public class UniqueEventList implements Iterable<Event> {
    private final ObservableList<Event> internalList = FXCollections.observableArrayList();
    private final ObservableList<Event> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);


    /**
     * Returns true if the list contains an equivalent event as the given argument.
     */
    public boolean contains(Event toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(event ->
                event.isSameEvent(toCheck));
    }

    /**
     * Adds an event to the list.
     * The event must not already exist in the list.
     */
    public void add(Event toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        internalList.add(toAdd);

    }

    /**
     * Replaces the event {@code target} in the list with {@code editedEvent}.
     * {@code target} must exist in the list.
     * The event identity of {@code editedEvent} must not be the same as another existing event in the list.
     */
    public void setEvent(Event target, Event editedEvent) {
        requireAllNonNull(target, editedEvent);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        if (!target.equals(editedEvent) && contains(editedEvent)) {
            throw new DuplicateEventException();
        }

        internalList.set(index, editedEvent);
    }

    /**
     * Removes the equivalent event from the list.
     * The event must exist in the list.
     */
    public void remove(Event toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new EventNotFoundException();
        }
    }

    /**
     * Removes the staff from all events stored in this UniqueEventList. Call for this function should only
     * originate from a DeleteStaffCommand.
     *
     */
    public void removeStaffFromAllEvents(Staff staff) {
        for (Event e : internalList) {
            if (e.isStaffInEvent(staff)) {
                e.removeStaff(staff);
            }
        }
    }

    /**
     * Removes the student from all events stored in this UniqueEventList. Call for this function should only
     * originate from a DeleteStudentCommand.
     *
     */
    public void removeStudentFromAllEvents(Student student) {
        for (Event e : internalList) {
            if (e.isStudentInEvent(student)) {
                e.removeStudent(student);
            }
        }
    }

    /**
     * Removes the external party from all events stored in this UniqueEventList. Call for this function should only
     * originate from a DeleteExternalPartyCommand.
     *
     */
    public void removeExternalPartyFromAllEvents(ExternalParty externalParty) {
        for (Event e : internalList) {
            if (e.isExternalPartyInEvent(externalParty)) {
                e.removeExternalParty(externalParty);
            }
        }
    }

    /**
     * Replaces the contents of this list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        requireAllNonNull(events);
        if (!eventsAreUnique(events)) {
            throw new DuplicateEventException();
        }
        internalList.setAll(events);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Event> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Event> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UniqueEventList)) {
            return false;
        }

        UniqueEventList otherUniqueEventList = (UniqueEventList) other;
        return internalList.equals(otherUniqueEventList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code events} contains only unique events.
     */
    private boolean eventsAreUnique(List<Event> events) {
        for (int i = 0; i < events.size() - 1; i++) {
            for (int j = i + 1; j < events.size(); j++) {
                if (events.get(i).equals(events.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

}
