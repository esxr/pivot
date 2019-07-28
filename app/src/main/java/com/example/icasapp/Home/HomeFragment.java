package com.example.icasapp.Home;

import androidx.fragment.app.Fragment;

/**

 * A simple {@link Fragment} subclass.

 */

public class HomeFragment extends Fragment {

  /*  FirebaseFirestore db;
    FirebaseUser user;

    View homeView;

    TextView textView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView imageView = homeView.findViewById(R.id.imageView);

        textView = homeView.findViewById(R.id.textView);



        DocumentReference docRef = db.collection("USER").document(user.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("msg", "DocumentSnapshot data: " + document.getData());
                        textView.setText(user.getDisplayName() + "\nDETAILS:"+ document.getData().toString());
                    } else {
                        Log.d("msg", "No such document");
                    }
                } else {
                    Log.d("msg", "get failed with ", task.getException());
                }
            }
        });
        try {
            new ImageLoadTask(user.getPhotoUrl().toString(), imageView).execute();
        }
        catch (Exception e)
        {

        }




        // Inflate the layout for this fragment
        return homeView;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }*/
}