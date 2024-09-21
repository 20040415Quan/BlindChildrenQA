package com.example.androidmvvmtest.db.room.database;

/**
 * @Author: wuleizhenshang
 * @Email: wuleizhenshang@163.com
 * @Date: 2024/03/16
 * @Discribe: 数据库类
 */

//TODO 需要使用再取消注释，这里为全局单例，加什么表在这里加，升级版本也在这里升级
//@Database(entities = {}
//        , version = 1, exportSchema = false)
//public abstract class AppDatabase extends RoomDatabase {
//需要使用这个类就继承RoomDatabase，这里先注释掉
public abstract class AppDatabase{


    //数据库名字
    private static final String DATABASE_NAME = "APP_DATABASE";
    //volatile线程间可见
    private static volatile AppDatabase mInstance;

    /**
     * 单例模式
     */
//    public static AppDatabase getInstance(Context context) {
//        if (mInstance == null) {
//            synchronized (AppDatabase.class) {
//                if (mInstance == null) {
//                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
//                                    AppDatabase.class, DATABASE_NAME)
//                            //需要迁移自己写再加，不过应该不用，因为就写一版给对面用
////                            .addMigrations(MIGRATION_1_2)//数据库版本迁移使用
//                            .build();
//                }
//            }
//        }
//        return mInstance;
//    }


    /**
     * 版本升级迁移1->2
     */
//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // Create the new table
//            database.execSQL("CREATE TABLE `wallpaper` " +
//                    "(uid INTEGER NOT NULL, " +
//                    "img TEXT, " +
//                    "PRIMARY KEY(`uid`))");
//        }
//    };

    //TODO Dao类写这里
}

