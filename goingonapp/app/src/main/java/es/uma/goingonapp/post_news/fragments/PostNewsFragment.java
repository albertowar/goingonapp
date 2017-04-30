package es.uma.goingonapp.post_news.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import es.uma.goingonapp.R;
import es.uma.goingonapp.common.entities.News;
import es.uma.goingonapp.common.network.NewsProxy;

/**
 * Created by Alb_Erc on 19/04/2015.
 */
public class PostNewsFragment extends Fragment implements View.OnClickListener {
    private ImageView photo;
    private TextView title;
    private TextView content;
    private Button postButton;

    private NewsProxy mNewsProxy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view
        View view = inflater.inflate(R.layout.fragment_post_news, container, false);

        this.title = (TextView)view.findViewById(R.id.post_news_title);
        this.content = (TextView)view.findViewById(R.id.post_news_content);
        this.photo = (ImageView)view.findViewById(R.id.post_news_photo);

        this.postButton = (Button)view.findViewById(R.id.post_news_button);
        this.postButton.setOnClickListener(this);

        this.setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_actionbar_post_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().finish();
                return true;
            case R.id.menu_item_photo:
                this.dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        News news = new News(
                this.title.getText().toString(),
                this.content.getText().toString(),
                "Malaga",
                dateFormat.format(new Date()),
                "AndroidApp");

        this.mNewsProxy = new NewsProxy(this.getActivity());
        this.mNewsProxy.createNews(news, this.postNewsListener, this.postNewsErrorListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("PostNewsActivityResult", "Path: " + this.mCurrentPhotoPath);

        Bitmap myBitmap = BitmapFactory.decodeFile(this.mCurrentPhotoPath);

        Log.d("PostNews", "Initial size: " + (myBitmap.getByteCount()/(1024*1024)));

        this.photo.setImageBitmap(myBitmap);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
               Log.d("PostNewsFragment", "Error during creation of the file: " + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    String mCurrentPhotoPath;

    // TODO: move somewhere else

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.d("Directory", "Null: " + (storageDir == null));
        Log.d("CreatedFile", "Null: " + (image == null));


        // Save a file: path for use with ACTION_VIEW intents
        this.mCurrentPhotoPath = image.getAbsolutePath();

        Log.d("CreateImage", "Path: " + mCurrentPhotoPath);
        return image;
    }

    // TODO: think about it
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }

    //region Listeners

    private Response.Listener<String> postNewsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Uri.parse(response);
            String[] parts = response.split("/");
            String newsIdString = parts[9];

            Log.d("PostNews", "Sending create image request to image: " + newsIdString);
            mNewsProxy.createNewsImage(mCurrentPhotoPath, UUID.fromString(newsIdString), postImageListener, postImageErrorListener);
        }
    };

    private Response.ErrorListener postNewsErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private Response.Listener<String> postImageListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            getActivity().finish();
        }
    };

    private Response.ErrorListener postImageErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    //endregion
}
