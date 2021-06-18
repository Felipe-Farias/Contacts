
package cl.ucn.disc.dsm.ffarias.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class.
 *
 * @author Felipe Farías Espinoza
 */
public class MainActivity extends AppCompatActivity {


  ListView lv_Contacts;
  ArrayList<Contact> contacts = new ArrayList<>();
  Cursor cursor = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    lv_Contacts = findViewById(R.id.lv_am_contacts);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        == PackageManager.PERMISSION_GRANTED) {

      getAllContacts();

    } else {
      ActivityCompat.requestPermissions(
          this,
              new String[] {Manifest.permission.READ_CONTACTS},
              30);
    }
  }

    /**
     * Obtain all the contacts inside the phone.
     */
  public final void getAllContacts() {
    ContentResolver contentResolver = getContentResolver();

    try {
      cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
              null,
              null,
              null,
              null);

    } catch (Exception ignored) {

    }

    if (cursor != null && cursor.getCount() > 0) {
      while (cursor.moveToNext()) {
        Contact contact = new Contact();
        String id_contact = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        contact.name =
            cursor.getString(cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME)
            );
        int hasNumber =
            Integer.parseInt(
                cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
            );

        if (hasNumber > 0) {
          Cursor numberCursor =
              contentResolver.query(
                  ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                  null,
                  ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                  new String[] {id_contact},
                  null);

          if (numberCursor != null) {
            numberCursor.moveToNext();
            contact.number =
                numberCursor.getString(
                    numberCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER)
                );
          }
          if (numberCursor != null)
            numberCursor.close();

        }
        contact.image = contactImage(id_contact);
        contacts.add(contact);
      }
      Adapter adapter = new Adapter(this, contacts);
      lv_Contacts.setAdapter(adapter);
    }
  }

    /**
     * Get the image of the contact
     * @param id_contact of the contact
     * @return the image.
     */
  public final Bitmap contactImage(String id_contact) {
    Uri contactUri =
        ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI,
                Long.parseLong(id_contact)
        );

    Uri imageUri =
        Uri.withAppendedPath(contactUri,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

    Cursor cursor =
        getContentResolver()
            .query(
                imageUri,
                    new String[] {
                            ContactsContract.Contacts.Photo.PHOTO},
                    null,
                    null,
                    null);

    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToNext();
      byte[] data = cursor.getBlob(0);

      if (data != null)
          return BitmapFactory.decodeStream(new ByteArrayInputStream(data));

      else
          return null;
    }

    if (cursor != null) cursor.close();
        return null;
  }
}



