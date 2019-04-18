package com.example.firebaseplatzi;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.firebaseplatzi.model.Artist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String ARTIST_NODE = "Artist";
    private static final int CHOOSER_IMAGER = 1;
    private DatabaseReference databaseReference;
    private Button btonAddArtist;
    private ListView lstArtist;
    private ArrayAdapter arrayAdapter;
    private List<String> arstistNames;
    private List<Artist> artistas;
    private AlertDialog.Builder dialogoConfirmArtist;

    private Button descargar, cargar;
    private ImageView tvImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btonAddArtist = (Button) findViewById(R.id.btnAddArtist);
        lstArtist = (ListView) findViewById(R.id.listArtist);

        descargar = (Button) findViewById(R.id.buttonDonwLoad);

        cargar = (Button) findViewById(R.id.buttonUpdload);

        tvImagen = (ImageView) findViewById(R.id.tvImage);

        dialogoConfirmArtist = new AlertDialog.Builder(this);

        arstistNames = new ArrayList<>();
        artistas = new ArrayList<>();



        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arstistNames);
        lstArtist.setAdapter(arrayAdapter);


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(ARTIST_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arstistNames.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        Artist artist = snapshot.getValue(Artist.class);
                        arstistNames.add(artist.getName());
                        artistas.add(artist);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        
        btonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createArtist();
            }
        });

        lstArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String nameArtist = "";

                final String id_artist = artistas.get(position).getId();
                nameArtist = artistas.get(position).getName();


                dialogoConfirmArtist.setTitle("Importante");
                dialogoConfirmArtist.setMessage("¿Esta seguro que desea eliminar el artista "+ nameArtist+"?");
                dialogoConfirmArtist.setCancelable(false);
                dialogoConfirmArtist.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogoConfirmArtist, int id) {

                        artistas.remove(position);
                        arstistNames.remove(position);
                        databaseReference.child(ARTIST_NODE).child(id_artist).removeValue();
                    }
                });
                dialogoConfirmArtist.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogoConfirmArtist, int id) {

                    }
                });
                dialogoConfirmArtist.show();
                return true;



            }
        });

        tvImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Seleccione una imagen"),CHOOSER_IMAGER);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSER_IMAGER){

            Uri imageUri = data.getData();
            if(imageUri != null){
                tvImagen.setImageURI(imageUri);
            }
        }
    }

    public void createArtist(){

        Artist artist = new Artist(databaseReference.push().getKey(), "Garbage", "Rock");

        databaseReference.child(ARTIST_NODE).child(artist.getId()).setValue(artist);

    }
}
