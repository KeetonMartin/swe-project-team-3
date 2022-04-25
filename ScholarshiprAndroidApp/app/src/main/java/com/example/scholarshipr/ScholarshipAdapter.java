package com.example.scholarshipr;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;*/

//import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ScholarshipAdapter extends RecyclerView.Adapter<ScholarshipAdapter.ViewHolder>{
    private Context context;
    private List<ScholarshipData> scholarshipData;

    public ScholarshipAdapter(Context context, List<ScholarshipData> scholarshipData) {
        this.context = context;
        this.scholarshipData = scholarshipData;
    }

    //Source used: https://www.geeksforgeeks.org/overriding-equals-method-in-java/
    // Overriding equals() to compare two ScholarshipAdapter objects
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of ScholarshipAdapter or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof ScholarshipAdapter)) {
            return false;
        }

        // typecast o to ScholarshipAdapter so that we can compare data members
        ScholarshipAdapter c = (ScholarshipAdapter) o;

        // Compare the data members and return accordingly
        int countOfDifferentShips = 0;

        for (int i = 0; i < scholarshipData.size(); i++) {
            if (! scholarshipData.get(i).equals(c.getScholarshipDataList().get(i)) ) {
                countOfDifferentShips += 1;
            };
        }

        return (countOfDifferentShips == 0);
    }

    public List<ScholarshipData> getNewListOfShips() {
        ArrayList<ScholarshipData> newList = new ArrayList<>();
        newList.addAll(scholarshipData);

        return newList;
    }

    // Involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ScholarshipAdapter", "onCreateViewHolder");
        View scholarshipView = LayoutInflater.from(context).inflate(R.layout.item_scholarship, parent, false);
        return new ViewHolder(scholarshipView);
    }

    // Involves populating data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ScholarshipAdapter", "onBindViewHolder" + position);

        ScholarshipData scholarship = scholarshipData.get(position);
        // Bind the movie data into the view holder
        holder.bind(scholarship);
    }

    @Override
    public int getItemCount() {
        return scholarshipData.size();
    }

    public List<ScholarshipData> getScholarshipDataList() {
        return scholarshipData;
    }

    public List<String> getScholarshipNames() {
        List<String> output = new ArrayList<>();

        for (ScholarshipData ship :
                scholarshipData) {
            output.add(ship.getName());
        }

        return output;
    }

    // Clean all elements of the recycler
    public void clear() {
        scholarshipData.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ScholarshipData> list) {
        scholarshipData.addAll(list);
        notifyDataSetChanged();
    }

    // only display filtered items when using search bar
    public void filterList(List<ScholarshipData> filteredList) {
        scholarshipData = filteredList;
        notifyDataSetChanged();
    }

    // Define view holder class
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvScholarshipInfo;
        private RelativeLayout container;
        private TextView tvScholarshipName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScholarshipInfo = itemView.findViewById(R.id.tvScholarshipInfo);
            container = itemView.findViewById(R.id.container);
            tvScholarshipName = itemView.findViewById(R.id.tvScholarshipName);
        }

        public void bind(ScholarshipData scholarship) {
            // Bind the contact name to the view element
            tvScholarshipName.setText(scholarship.getName());
            tvScholarshipInfo.setText(scholarship.getDescription());



            // Register the click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Navigate to a new activity on tap
                    Intent i = new Intent(context, ScholarshipDetails.class);
                    i.putExtra("scholarshipName",scholarship.getName());
                    i.putExtra("scholarshipDesc",scholarship.getDescription());
                    i.putExtra("amount",scholarship.getAmount());
                    i.putExtra("gpa",scholarship.getGpaRequirement());
                    //i.putExtra("date",scholarship.getDueDate());
                    context.startActivity(i);
                }
            });
        }
    }
}
