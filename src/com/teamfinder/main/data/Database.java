package com.teamfinder.main.data;

import com.teamfinder.main.user.Activity;
import com.teamfinder.main.user.Event;
import com.teamfinder.main.user.User;
import org.la4j.Vector;
import org.mapdb.*;
import org.mapdb.serializer.SerializerArray;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public class Database {

    public static final DB db;

    protected static final HTreeMap<String, PersistentUser> users;
    protected static final BTreeMap<String[], PersistentEvent> events;

    protected static final Boolean useless = Boolean.FALSE;

    static {
        db = DBMaker.fileDB("team-finder.db")
                .fileMmapEnableIfSupported()
                .make();

        users = db.hashMap("users")
                .keySerializer(Serializer.STRING_ASCII)
                .valueSerializer(Serializer.JAVA)
                .layout(8, 8, 8)
                .counterEnable()
                .createOrOpen();

        events = db.treeMap("events")
                .keySerializer(new SerializerArray<>(Serializer.STRING_ASCII))
                .valueSerializer(Serializer.JAVA)
                .counterEnable()
                .createOrOpen();
    }

    public static void add(final Event event) {
        events.put(event.attendeesEmails(), new PersistentEvent(event));
    }

    public static void add(final User user) {
        users.put(user.email(), new PersistentUser(user));
    }

    public static User getUser(final String email) {
        return users.get(email);
    }

    public static Set<String> getUserCollection() {
        return users.keySet();
    }

    public static Set<String[]> getEventCollection() {
        return events.keySet();
    }

    public static Stream<User> getUsers() {
        return users.values().parallelStream();
    }

    public static Stream<Event> getEvents() {
        return events.values().parallelStream();
    }

    public static class PersistentEvent implements Event {

        protected final String organizerEmail;
        protected final String[] attendeesEmails;
        protected final long time;
        protected final double latitude, longitude;
        protected final String type;

        public PersistentEvent(final Event event) {
            this.organizerEmail = event.organizerEmail();
            this.attendeesEmails = event.attendeesEmails();
            this.time = event.time();
            this.latitude = event.location().latitude;
            this.longitude = event.location().longitude;
            this.type = event.type().toString();
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
            return Location.newLocationRadians(latitude, longitude);
        }

        public Activity type() {
            return Activity.valueOf(type);
        }
    }

    public static class PersistentUser implements User {

        protected final String email;
        protected final String name;
        protected final int age;
        protected final double personality;
        protected final double latitude, longitude;
        protected final double[] interestVector;
        protected final String[] beef;
        protected final long phoneNumber;

        public PersistentUser(final User user) {
            this.email = user.email();
            this.name = user.name();
            this.age = user.age();
            this.personality = user.personality();
            this.latitude = user.location().latitude;
            this.longitude = user.location().longitude;
            this.interestVector = user.interestVector().toDenseVector().toArray();
            this.beef = user.beef().stream().toArray(String[]::new);
            this.phoneNumber = user.phoneNumber();
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
            return Location.newLocationRadians(latitude, longitude);
        }

        public Vector interestVector() {
            return Vector.fromArray(interestVector);
        }

        public Set<String> beef() {
            return Arrays.stream(beef).collect(Collectors.toSet());
        }

        public long phoneNumber() {
            return phoneNumber;
        }
    }
}
