package pt.ulisboa.tecnico.cmov.locmess.utils;

import android.animation.ObjectAnimator;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

/**
 * Created by iluxo on 18/05/2017.
 */

public final class InternalStorage {
    private InternalStorage() { }

    public static void writeToDisk(Context context, String fileName, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readFromDisk(Context context, String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static boolean fileExists(Context context, String fileName) {
       // TODO: this is horrible, but we're in a massive hurry, I'M SORRY!
        try {
           context.openFileInput(fileName);
           return true;
       } catch (FileNotFoundException e) {
           return false;
       }
    }
}
