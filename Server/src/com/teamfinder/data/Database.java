package com.teamfinder.data;

import com.teamfinder.user.Activity;
import com.teamfinder.user.Event;
import com.teamfinder.user.User;
import org.la4j.Vector;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArray;
import org.mapdb.serializer.SerializerArrayTuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public class Database {

    protected static final DB db;

    protected static final NavigableSet<Object[]> users;
    protected static final NavigableSet<Object[]> events;

    static {
        db = DBMaker.fileDB("team-finder.db")
                .closeOnJvmShutdown()
                .fileMmapEnableIfSupported()
                .allocateStartSize(50_000_000)
                .allocateIncrement(5_000_000)
                .transactionEnable()
                .make();

        users = db.treeSet("users")
                .serializer(new SerializerArrayTuple(
                        Serializer.STRING_ASCII, // email
                        Serializer.STRING, // name
                        Serializer.INTEGER_DELTA, // age
                        Serializer.DOUBLE, // personality
                        Serializer.DOUBLE, // latitude radians
                        Serializer.DOUBLE, // longitude radians
                        Serializer.DOUBLE_ARRAY, // interest interestVector
                        new SerializerArray<>(Serializer.STRING_ASCII) // conflicts with other users
                ))
                .counterEnable()
                .maxNodeSize(6)
                .createOrOpen();

        events = db.treeSet("events")
                .serializer(new SerializerArrayTuple(
                        Serializer.STRING_ASCII, // organizer email
                        new SerializerArray<>(Serializer.STRING_ASCII), // attendees emails
                        Serializer.LONG, // unix time
                        Serializer.DOUBLE, // latitude radians
                        Serializer.DOUBLE, // longitude radians
                        Serializer.STRING_ASCII // type
                ))
                .counterEnable()
                .maxNodeSize(6)
                .createOrOpen();
    }

    protected static void add(final Event event) {
        events.add(new Object[]{
                event.organizerEmail(),
                event.attendeesEmails(),
                event.time(),
                event.location().latitude, // radians
                event.location().longitude, // radians
                event.type()
        });
    }

    protected static void add(final User user) {
        users.add(new Object[]{
                user.email(),
                user.name(),
                user.age(),
                user.personality(),
                user.location().latitude, // radians
                user.location().longitude, // radians
                user.interestVector().toDenseVector().toArray(),
                user.beef().stream().toArray(String[]::new),
                user.phoneNumber()
        });
    }

    public static NavigableSet<Object[]> getUserCollection() {
        return Collections.unmodifiableNavigableSet(users);
    }

    public static NavigableSet<Object[]> getEventCollection() {
        return Collections.unmodifiableNavigableSet(events);
    }

    public static Stream<User> getUsers() {
        return users.parallelStream()
                .map(PersistentUser::new);
    }

    public static Stream<Event> getEvents() {
        return events.parallelStream()
                .map(PersistentEvent::new);
    }

    public static class PersistentUser implements User {

        protected final Object[] dbEntry;

        protected final String email;
        protected String name;
        protected int age;
        protected double personality;
        protected Location location;
        protected Vector interestVector;
        protected Set<String> beef;
        protected long phoneNumber;

        public PersistentUser(final Object[] dbEntry) {
            this.dbEntry = dbEntry;

            email = (String) dbEntry[0];
            name = (String) dbEntry[1];
            age = (Integer) dbEntry[2];
            personality = (Double) dbEntry[3];
            location = Location.newLocationRadians((Double) dbEntry[4], (Double) dbEntry[5]);
            interestVector = Vector.fromArray((double[]) dbEntry[6]);
            beef = Arrays.stream((String[]) dbEntry[7]).collect(Collectors.toSet());
            phoneNumber = (Long) dbEntry[8];
        }

        public void name(String name) {
            if (name == null || (name = name.trim()).equals("")) return;
            this.name = name;
        }

        public void age(final int age) {
            if (age < 13 || age > 100) return;
            this.age = age;
        }

        public void personality(final double personality) {
            if (personality < 0 || personality >= 1) return;
            this.personality = personality;
        }

        public void location(final Location location) {
            if (location == null) return;
            this.location = location;
        }

        public void interestVector(final Vector interestVector) {
            if (interestVector == null || interestVector.length() != Activity.values().length) return;
            this.interestVector = interestVector;
        }

        public void phoneNumber(final long phoneNumber) {
            if (phoneNumber < 100_000_0000L || phoneNumber > 999_999_9999L) return;
            this.phoneNumber = phoneNumber;
        }

        public String email() {
            return email;
        }

        public String name() {
            return name;
        }

        public int age() {
            return age;
        }

        public double personality() {
            return personality;
        }

        public Location location() {
            return location;
        }

        public Vector interestVector() {
            return interestVector;
        }

        public Set<String> beef() {
            return beef;
        }

        public long phoneNumber() {
            return phoneNumber;
        }

        public void save() {
            users.remove(dbEntry);
            add(this);
        }
    }

    public static class PersistentEvent implements Event {

        final String organizerEmail;
        final String[] attendeesEmails;
        final long time;
        final Location location;
        final Activity type;

        public PersistentEvent(final Object[] dbEntry) {
            organizerEmail = (String) dbEntry[0];
            attendeesEmails = (String[]) dbEntry[1];
            time = (long) dbEntry[2];
            location = Location.newLocationRadians((Double) dbEntry[3], (Double) dbEntry[4]);
            type = Activity.valueOf((String) dbEntry[5]);
        }

        public String organizerEmail() {
            return organizerEmail;
        }

        public String[] attendeesEmails() {
            return attendeesEmails.clone();
        }

        public long time() {
            return time;
        }

        public Location location() {
            return location;
        }

        public Activity type() {
            return type;
        }
    }
}
