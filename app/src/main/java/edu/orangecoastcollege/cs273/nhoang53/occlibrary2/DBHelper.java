package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Joseph on 1/16/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    static final String DATABASE_NAME = "OCCLibrary";
    private static final String DATABASE_BOOK_TABLE = "Book";
    private static final String DATABASE_ROOM_TABLE = "Room";
    private static final String DATABASE_ROOM_BOOKING_TABLE = "RoomBooking";
    private static final String DATABASE_STUDENT_TABLE = "Student";
    private static final int DATABASE_VERSION = 1;

        /*//TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE BOOK TABLE
        private static final String BOOK_KEY_FIELD_ID = "id";
        private static final String BOOK_FIELD_TITLE = "title";
        private static final String BOOK_FIELD_DESCRIPTION = "description";
        private static final String BOOK_FIELD_AUTHOR = "author";
        private static final String BOOK_FIELD_ISBN = "isbn";
        private static final String BOOK_FIELD_QTY_AVAIL = "quantity";
        private static final String BOOK_FIELD_IMAGE_URI = "image_uri";*/

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE ROOM TABLE
    private static final String ROOM_KEY_FIELD_ID = "id";
    private static final String ROOM_FIELD_NAME = "name";
    private static final String ROOM_FIELD_DESCRIPTION = "description"; // location and list of support devices
    private static final String ROOM_FIELD_CAPACITY = "capacity";

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE ROOM BOOKING TABLE
    private static final String ROOM_BOOKING_KEY_FIELD_ID = "room_booking_id";
    private static final String ROOM_BOOKING_FIELD_ROOM_ID = "room_id";
    private static final String ROOM_BOOKING_FIELD_STUDENT_ID = "student_id";
    private static final String ROOM_BOOKING_FIELD_DATE = "date";
    private static final String ROOM_BOOKING_FIELD_START_TIME = "start_time";
    private static final String ROOM_BOOKING_FIELD_HOURS_USED = "hours_used";

    // DEFINE THE FIELDS (COLUMN NAMES) FOR THE STUDENT TABLE
    private static final String KEY_FIELD_ID = "student_id";
    private static final String KEY_FIELD_LAST_NAME = "last_name";
    private static final String KEY_FIELD_FIRST_NAME = "first_name";
    private static final String KEY_FIELD_PASSWORD = "password";

    /**
     * Default constructor that get Context from activity, create database name and version.
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    /**
     *  Create data table: room. room booking, student
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    /*        // BOOK
            String table = "CREATE TABLE " + DATABASE_BOOK_TABLE + "("
                    + BOOK_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + BOOK_FIELD_TITLE + " TEXT, "
                    + BOOK_FIELD_DESCRIPTION + " TEXT, "
                    + BOOK_FIELD_AUTHOR + " TEXT, "
                    + BOOK_FIELD_ISBN + " INTEGER, "
                    + BOOK_FIELD_QTY_AVAIL + " INTEGER, "
                    + BOOK_FIELD_IMAGE_URI + " TEXT" + ")";
            sqLiteDatabase.execSQL (table);*/

        // ROOM
        String table = "CREATE TABLE " + DATABASE_ROOM_TABLE + "("
                + ROOM_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROOM_FIELD_NAME + " TEXT, "
                + ROOM_FIELD_DESCRIPTION + " TEXT, "
                + ROOM_FIELD_CAPACITY + " INTEGER" + ")";
        sqLiteDatabase.execSQL (table);

        //STUDENT
        table = "CREATE TABLE " + DATABASE_STUDENT_TABLE + "("
                + KEY_FIELD_ID + " INTEGER, "
                + KEY_FIELD_FIRST_NAME + " TEXT, "
                + KEY_FIELD_LAST_NAME + " TEXT, "
                + KEY_FIELD_PASSWORD + " TEXT)";
        sqLiteDatabase.execSQL(table);

        //ROOM_BOOKING
        table = "CREATE TABLE " + DATABASE_ROOM_BOOKING_TABLE + "("
                + ROOM_BOOKING_KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROOM_BOOKING_FIELD_ROOM_ID + " INTEGER, "
                + ROOM_BOOKING_FIELD_STUDENT_ID + " INTEGER, "
                + ROOM_BOOKING_FIELD_DATE + " TEXT, "
                + ROOM_BOOKING_FIELD_START_TIME + " TEXT, "
                + ROOM_BOOKING_FIELD_HOURS_USED + " REAL, "
                + "FOREIGN KEY(" + ROOM_BOOKING_FIELD_ROOM_ID + ") REFERENCES "
                + DATABASE_ROOM_TABLE + "(" + ROOM_KEY_FIELD_ID + "),"
                + "FOREIGN KEY(" + ROOM_BOOKING_FIELD_STUDENT_ID + ") REFERENCES "
                + DATABASE_STUDENT_TABLE + "(" + KEY_FIELD_ID + ")"+ ")";
        sqLiteDatabase.execSQL (table);

    }

    /**
     * Delete Data tables if their already exists in the phone, then create new data tables.
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_BOOK_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_ROOM_BOOKING_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_ROOM_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_STUDENT_TABLE);

        onCreate(sqLiteDatabase);
    }

    //******* import data from csv file ********

    /**
     * import data for Room table in the CSV file
     * @param csvFileName
     * @return
     */
    public boolean importRoomsFromCSV(String csvFileName) {
        AssetManager manager = mContext.getAssets();
        InputStream inStream;
        try {
            inStream = manager.open(csvFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line;
        try {
            while ((line = buffer.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 4) {
                    Log.d("OCC Library", "Skipping Bad CSV Row: " + Arrays.toString(fields));
                    continue;
                }
                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                String description = fields[2].trim();
                int capacity = Integer.parseInt(fields[3].trim());
                addRoom(new Room(id, name, description, capacity));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Import data for Student table from CSV file
     * @param csvFileName
     * @return
     */
    public boolean importStudentFromCSV(String csvFileName)
    {
        AssetManager am = mContext.getAssets();
        InputStream stream = null;
        try{
            stream = am.open(csvFileName);
        }catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
        String line;
        try{
            while((line = buffer.readLine()) != null){
                String[] fields = line.split(",");
                if(fields.length != 4)
                {
                    Log.d("OCC Library", "Skipping bad csv row: " + Arrays.toString(fields));
                    continue;
                }

                int id = Integer.parseInt(fields[0].trim()); // trim will cut off space behind
                String lastName = fields[1].trim();
                String firstName = fields[2].trim();
                String password = fields[3].trim();

                addStudent(new Student(id, lastName, firstName, password));
            }
        }catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * import data for Room Booking table from CSV file
     * @param csvFileName
     * @return
     */
    public boolean importRoomBookingsFromCSV(String csvFileName) {
        AssetManager manager = mContext.getAssets();
        InputStream inStream;
        try {
            inStream = manager.open(csvFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line;
        try {
            while ((line = buffer.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 6) {
                    Log.d("OCC Library", "Skipping Bad CSV Row: " + Arrays.toString(fields));
                    continue;
                }

                int id = Integer.parseInt(fields[0].trim());
                int roomId = Integer.parseInt(fields[1].trim());
                int studentId = Integer.parseInt(fields[2].trim());
                String date = fields[3].trim();
                String startTime = fields[4].trim();
                int hoursUsed = Integer.parseInt(fields[5].trim());
                addRoomBooking(new RoomBooking(id, roomId, studentId, date, startTime, hoursUsed));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //********* STUDENT TABLE OPERATIONS: ADD, GET 1, GET ALL, UPDATE, DELETE **********

    /**
     * insert student into Student table
     * @param student
     */
    public void addStudent(Student student)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FIELD_ID, student.getId());
        values.put(KEY_FIELD_LAST_NAME, student.getLastName());
        values.put(KEY_FIELD_FIRST_NAME, student.getFirstName());
        values.put(KEY_FIELD_PASSWORD, student.getPassword());

        db.insert(DATABASE_STUDENT_TABLE, null, values);
        db.close();
    }

    /**
     * Get all students in Student table and put it in ArrayList
     * @return ArrayList<Student>
     */
    public ArrayList<Student> getAllStudents(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Student> studentsList = new ArrayList<>();

        Cursor cursor = db.query(DATABASE_STUDENT_TABLE, null, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do{
                Student student = new Student(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                studentsList.add(student);
            }while (cursor.moveToNext());
        }

        db.close();

        return studentsList;
    }

        /*public void updateNoShowTimes(Student student)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_FIELD_NO_SHOW_TIMES, student.getNoShowTimes() + 1);

            db.update(DATABASE_STUDENT_TABLE, values, KEY_FIELD_ID + " =?",
                    new String[] {String.valueOf(student.getId())});
            db.close();
        }*/

    /**
     * get all information of Student base on studentId
     * @param studentId
     * @return Student
     */
    public Student getStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_STUDENT_TABLE,
                new String[]{KEY_FIELD_ID, KEY_FIELD_LAST_NAME, KEY_FIELD_FIRST_NAME, KEY_FIELD_PASSWORD},
                KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(studentId)}, null, null, null, null);

        Student student = null;
        if(cursor != null)
        {
            cursor.moveToFirst();
            student = new Student(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
        }

            /*Student student = null;
            if (cursor.moveToFirst()) {
                do {
                    student = new Student(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4));
                } while (cursor.moveToNext());
            }*/

        db.close();
        return student;
    }

    /**
     * Change student with the newPassword
     * @param studentId
     * @param newPassword
     */
    public void changePassword(int studentId, String newPassword)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIELD_PASSWORD, newPassword);

        db.update(DATABASE_STUDENT_TABLE, values, KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(studentId)});
    }

    //********** ROOM DATABASE OPERATIONS:  ADD, GETALL, EDIT, DELETE
    public void addRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ROOM_KEY_FIELD_ID, room.getId());
        values.put(ROOM_FIELD_NAME, room.getName());
        values.put(ROOM_FIELD_DESCRIPTION, room.getDescription());
        values.put(ROOM_FIELD_CAPACITY, room.getCapacity());

        db.insert(DATABASE_ROOM_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public ArrayList<Room> getAllRooms() {
        ArrayList<Room> roomsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(DATABASE_ROOM_TABLE, null, null, null, null, null, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()) {
            do {
                Room room =
                        new Room(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getInt(3));
                roomsList.add(room);
            } while (cursor.moveToNext());
        }

        database.close();
        return roomsList;
    }

    public void deleteRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETE THE TABLE ROW
        db.delete(DATABASE_ROOM_TABLE, ROOM_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(room.getId())});
        db.close();
    }

    public void deleteAllRooms() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_ROOM_TABLE, null, null);
        db.close();
    }

    public void updateRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ROOM_FIELD_NAME, room.getName());
        values.put(ROOM_FIELD_DESCRIPTION, room.getDescription());
        values.put(ROOM_FIELD_CAPACITY, room.getCapacity());

        db.update(DATABASE_ROOM_TABLE, values, ROOM_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(room.getId())});
        db.close();
    }

    public Room getRoom(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DATABASE_ROOM_TABLE,
                new String[]{ROOM_KEY_FIELD_ID, ROOM_FIELD_NAME, ROOM_FIELD_DESCRIPTION, ROOM_FIELD_CAPACITY},
                ROOM_KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

            /*if (cursor != null)
                cursor.moveToFirst();

            Room room = new Room(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3));*/
        Room room = null;
        if (cursor.moveToFirst()) {
            do {
                room = new Room(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3));
            } while (cursor.moveToNext());
        }

        db.close();
        return room;
    }

    //********** ROOM BOOKING DATABASE OPERATIONS:  ADD, GETALL, EDIT, DELETE
    public void addRoomBooking(RoomBooking roomBooking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(ROOM_BOOKING_KEY_FIELD_ID, roomBooking.getId());
        values.put(ROOM_BOOKING_FIELD_ROOM_ID, roomBooking.getRoomId());
        values.put(ROOM_BOOKING_FIELD_STUDENT_ID, roomBooking.getStudentId());
        values.put(ROOM_BOOKING_FIELD_DATE, roomBooking.getDate());
        values.put(ROOM_BOOKING_FIELD_START_TIME, roomBooking.getStartTime());
        values.put(ROOM_BOOKING_FIELD_HOURS_USED, roomBooking.getHoursUsed());

        db.insert(DATABASE_ROOM_BOOKING_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public ArrayList<RoomBooking> getAllRoomBookings() {

        ArrayList<RoomBooking> roomBookingsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DATABASE_ROOM_BOOKING_TABLE,
                null,
                null,
                null, null, null, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()) {
            do {
                RoomBooking roomBooking =
                        new RoomBooking(cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getFloat(5) );
                roomBookingsList.add(roomBooking);
            } while (cursor.moveToNext());
        }
        database.close();
        return roomBookingsList;
    }

    public void deleteRoomBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETE THE TABLE ROW
        db.delete(DATABASE_ROOM_BOOKING_TABLE, ROOM_BOOKING_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(bookingId)});
        db.close();
    }

    public void deleteAllRoomBookings() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_ROOM_BOOKING_TABLE, null, null);
        db.close();
    }

    public void updateRoomBooking(RoomBooking roomBooking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ROOM_BOOKING_FIELD_ROOM_ID, roomBooking.getRoomId());
        values.put(ROOM_BOOKING_FIELD_STUDENT_ID, roomBooking.getStudentId());
        values.put(ROOM_BOOKING_FIELD_DATE, roomBooking.getDate());
        values.put(ROOM_BOOKING_FIELD_START_TIME, roomBooking.getStartTime());
        values.put(ROOM_BOOKING_FIELD_HOURS_USED, roomBooking.getHoursUsed());

        db.update(DATABASE_ROOM_BOOKING_TABLE, values, ROOM_BOOKING_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(roomBooking.getId())});
        db.close();
    }

    public RoomBooking getRoomBooking(int roomBookingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DATABASE_ROOM_BOOKING_TABLE,
                new String[]{ROOM_BOOKING_KEY_FIELD_ID, ROOM_BOOKING_FIELD_ROOM_ID, ROOM_BOOKING_FIELD_STUDENT_ID,
                        ROOM_BOOKING_FIELD_DATE, ROOM_BOOKING_FIELD_START_TIME, ROOM_BOOKING_FIELD_HOURS_USED},
                ROOM_BOOKING_KEY_FIELD_ID + " =?",
                new String[]{String.valueOf(roomBookingId)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        RoomBooking roomBooking = null;
        if (cursor.moveToFirst()) {
            do {
                roomBooking = new RoomBooking(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getFloat(5));
            } while (cursor.moveToNext());
        }

        db.close();
        return roomBooking;
    }


    //********** BOOK DATABASE OPERATIONS:  ADD, GETALL, EDIT, DELETE

        /*public void addBook(Book book) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            //ADD KEY-VALUE PAIR INFORMATION FOR THE TABLE
            values.put(BOOK_FIELD_TITLE, book.getTitle());
            values.put(BOOK_FIELD_DESCRIPTION, book.getDescription());
            values.put(BOOK_FIELD_AUTHOR, book.getAuthor());
            values.put(BOOK_FIELD_ISBN, book.getISBN());
            values.put(BOOK_FIELD_QTY_AVAIL, book.getQty());
            values.put(BOOK_FIELD_IMAGE_URI, book.getImageUri().toString());

            // INSERT THE ROW IN THE TABLE
            db.insert(DATABASE_BOOK_TABLE, null, values);

            // CLOSE THE DATABASE CONNECTION
            db.close();
        }

        public ArrayList<Book> getAllBooks() {
            ArrayList<Book> bookList = new ArrayList<>();
            SQLiteDatabase database = this.getReadableDatabase();
            //Cursor cursor = database.rawQuery(queryList, null);
            Cursor cursor = database.query(
                    DATABASE_BOOK_TABLE,
                    new String[]{BOOK_KEY_FIELD_ID, BOOK_FIELD_TITLE, BOOK_FIELD_DESCRIPTION, BOOK_FIELD_AUTHOR, BOOK_FIELD_ISBN, BOOK_FIELD_QTY_AVAIL, BOOK_FIELD_IMAGE_URI},
                    null,
                    null,
                    null, null, null, null );

            //COLLECT EACH ROW IN THE TABLE
            if (cursor.moveToFirst()){
                do {
                    Book book =
                            new Book(cursor.getInt(0), //iD
                                    cursor.getString(1),//title
                                    cursor.getString(2),//desc
                                    cursor.getString(3),//author
                                    cursor.getInt(4),//isbn
                                    cursor.getInt(5),//available
                                    Uri.parse(cursor.getString(6))//imageUri
                            );
                    bookList.add(book);
                } while (cursor.moveToNext());
            }
            return bookList;
        }

        public void deleteBook(Book book){
            SQLiteDatabase db = this.getWritableDatabase();

            // DELETE THE TABLE ROW
            db.delete(DATABASE_BOOK_TABLE, BOOK_KEY_FIELD_ID + " = ?",
                    new String[] {String.valueOf(book.getId())});
            db.close();
        }

        public void deleteAllBooks()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(DATABASE_BOOK_TABLE, null, null);
            db.close();
        }

        public void updateBook(Book book){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(BOOK_FIELD_TITLE, book.getTitle());
            values.put(BOOK_FIELD_DESCRIPTION, book.getDescription());
            values.put(BOOK_FIELD_AUTHOR, book.getAuthor());
            values.put(BOOK_FIELD_ISBN, book.getISBN());
            values.put(BOOK_FIELD_QTY_AVAIL, book.getQty());
            values.put(BOOK_FIELD_IMAGE_URI, book.getImageUri().toString());

            db.update(DATABASE_BOOK_TABLE, values, BOOK_KEY_FIELD_ID + " = ?",
                    new String[]{String.valueOf(book.getId())});
            db.close();
        }

        public Book getBook(int id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(
                    DATABASE_BOOK_TABLE,
                    new String[]{BOOK_KEY_FIELD_ID, BOOK_FIELD_TITLE, BOOK_FIELD_DESCRIPTION, BOOK_FIELD_AUTHOR, BOOK_FIELD_ISBN, BOOK_FIELD_QTY_AVAIL, BOOK_FIELD_IMAGE_URI},
                    KEY_FIELD_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null, null );

            if (cursor != null)
                cursor.moveToFirst();

            Book book = new Book(
                    cursor.getInt(0), //iD
                    cursor.getString(1),//title
                    cursor.getString(2),//desc
                    cursor.getString(3),//author
                    cursor.getInt(4),//isbn
                    cursor.getInt(5),//available
                    Uri.parse(cursor.getString(6))//imageUri
            );

            db.close();
            return book;
        }*/
}