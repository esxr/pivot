package com.example.icasapp.Forums;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.icasapp.Firebase.SpiderDocument;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirebaseForumsHelper {
    FirebaseFirestore firebaseFirestore;

    public FirebaseFirestore setFirebaseFirestore() {
        return firebaseFirestore=FirebaseFirestore.getInstance();
    }

    public void emptyTopicDelete()
    {
        firebaseFirestore = setFirebaseFirestore();

        final CollectionReference collectionReference = firebaseFirestore.collection("General/General/Posts");
        CollectionReference collectionReference2 = firebaseFirestore.collection("General/Alumni/Posts");

        firebaseFirestore.collection("Specific").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                 @Override
                                                                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                     for(final DocumentSnapshot documentSnapshot : task.getResult())
                                                                                     {
                                                                                         documentSnapshot.getReference().collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                             @Override
                                                                                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                 for(DocumentSnapshot documentSnapshot1 : task.getResult()){

                                                                                                     CollectionReference collectionReference1 = documentSnapshot1.getReference().collection("Posts");
                                                                                                     SpiderDocument.spider(collectionReference1,0,"delete",true,null);
                                                                                                 }
                                                                                             }
                                                                                         });
                                                                                     }
                                                                                 }
                                                                             });

        SpiderDocument.spider(collectionReference, 0, "delete", true,null);
        SpiderDocument.spider(collectionReference2,0,"delete",true,null);


    }

    public void topicDelete()
    {
        firebaseFirestore = setFirebaseFirestore();

        final CollectionReference collectionReference = firebaseFirestore.collection("General/General/Posts");
        CollectionReference collectionReference2 = firebaseFirestore.collection("General/Alumni/Posts");

        firebaseFirestore.collection("Specific").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(final DocumentSnapshot documentSnapshot : task.getResult())
                {
                    documentSnapshot.getReference().collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot documentSnapshot1 : task.getResult()){

                                CollectionReference collectionReference1 = documentSnapshot1.getReference().collection("Posts");
                                SpiderDocument.spider(collectionReference1,0,"delete",false,null);
                            }
                        }
                    });
                }
            }
        });

        SpiderDocument.spider(collectionReference, 0, "delete", false,null);
        SpiderDocument.spider(collectionReference2,0,"delete",false,null);
    }

    public void topidAdd(final HashMap obj)
    {
        firebaseFirestore = setFirebaseFirestore();

        final CollectionReference collectionReference = firebaseFirestore.collection("General/General/Posts");
        CollectionReference collectionReference2 = firebaseFirestore.collection("General/Alumni/Posts");

        firebaseFirestore.collection("Specific").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(final DocumentSnapshot documentSnapshot : task.getResult())
                {
                    documentSnapshot.getReference().collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot documentSnapshot1 : task.getResult()){

                                CollectionReference collectionReference1 = documentSnapshot1.getReference().collection("Posts");
                                SpiderDocument.spider(collectionReference1,0,"change",false,obj);
                            }
                        }
                    });
                }
            }
        });

        SpiderDocument.spider(collectionReference, 0, "change", false,obj);
        SpiderDocument.spider(collectionReference2,0,"change",false,obj);
    }

    public void questionEmptyDelete()
    {
        firebaseFirestore = setFirebaseFirestore();

        firebaseFirestore.collection("General/General/Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for( DocumentSnapshot documentSnapshot : task.getResult()){
                    Log.i("DEVOPS",documentSnapshot.getId());
                    Map<String, Object> map = documentSnapshot.getData();
                    Log.i("DEVOPS",map.toString());
                    if(map.size()==0){
                        Log.i("DEVOPS","REACHED EMPTY MAP");
                       CollectionReference collectionReference = documentSnapshot.getReference().collection("Questions");
                        SpiderDocument.spider(collectionReference,0,"delete",false,null);
                    }
                  CollectionReference collectionReference =  documentSnapshot.getReference().collection("Questions");
                  SpiderDocument.spider(collectionReference,0,"delete",true,null);
                }
            }
        });


       firebaseFirestore.collection("General/Alumni/Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for( DocumentSnapshot documentSnapshot : task.getResult()){
                    CollectionReference collectionReference =  documentSnapshot.getReference().collection("Questions");
                    SpiderDocument.spider(collectionReference,0,"delete",true,null);
                }
           }
       });


        firebaseFirestore.collection("Specific").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(final DocumentSnapshot documentSnapshot : task.getResult())
                {
                    documentSnapshot.getReference().collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot documentSnapshot1 : task.getResult()){

                                documentSnapshot1.getReference().collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                            for( DocumentSnapshot documentSnapshot2 : task.getResult()){

                                                                                                                                CollectionReference collectionReference1 = documentSnapshot2.getReference().collection("Questions");
                                                                                                                                SpiderDocument.spider(collectionReference1,0,"delete",false,null);
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                            }
                        }
                    });
                }
            }
        });

    }
    public void answerEmptyDelete()
    {

        firebaseFirestore = setFirebaseFirestore();

        firebaseFirestore.collection("General/General/Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for( final DocumentSnapshot documentSnapshot : task.getResult()){
                    documentSnapshot.getReference().collection("Questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                                                       @Override
                                                                                                                                                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                                                           for( DocumentSnapshot documentSnapshot1 : task.getResult()){

                                                                                                                                                               CollectionReference collectionReference = documentSnapshot1.getReference().collection("Answers");

                                                                                                                                                               SpiderDocument.spider(collectionReference, 0, "delete", true, null);
                                                                                                                                                           }
                                                                                                                                                       }
                                                                                                                                                   });

                }
            }
        });


        firebaseFirestore.collection("General/Alumni/Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for( DocumentSnapshot documentSnapshot : task.getResult()){
                    documentSnapshot.getReference().collection("Questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for( DocumentSnapshot documentSnapshot1 : task.getResult()){

                                CollectionReference collectionReference = documentSnapshot1.getReference().collection("Answers");

                                SpiderDocument.spider(collectionReference, 0, "delete", true, null);
                            }
                        }
                    });


                }
            }
        });


        firebaseFirestore.collection("Specific").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(final DocumentSnapshot documentSnapshot : task.getResult())
                {
                    documentSnapshot.getReference().collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot documentSnapshot1 : task.getResult()){

                                documentSnapshot1.getReference().collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for( DocumentSnapshot documentSnapshot2 : task.getResult()){
                                            documentSnapshot2.getReference().collection("Questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for( DocumentSnapshot documentSnapshot1 : task.getResult()){

                                                        CollectionReference collectionReference = documentSnapshot1.getReference().collection("Answers");

                                                        SpiderDocument.spider(collectionReference, 0, "delete", true, null);
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

}
