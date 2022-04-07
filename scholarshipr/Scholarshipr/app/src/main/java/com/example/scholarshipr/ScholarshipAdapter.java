package com.example.scholarshipr;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;*/

//import org.parceler.Parcels;

import java.util.List;

public class ScholarshipAdapter extends RecyclerView.Adapter<ScholarshipAdapter.ViewHolder>{
    private Context context;
    private List<ScholarshipData> scholarshipData;

    public ScholarshipAdapter(Context context, List<ScholarshipData> scholarshipData) {
        this.context = context;
        this.scholarshipData = scholarshipData;
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
        // Get the contact at the passed in position
        ScholarshipData scholarship = scholarshipData.get(position);
        // Bind the movie data into the view holder
        holder.bind(scholarship);
    }

    @Override
    public int getItemCount() {
        return scholarshipData.size();
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

            /*
            Use Glide to load an image into the ImageView
            (currently set to a default profile image, can change to load from Parse Backend later)
            and crop the image into a circle
            */

            // Register the click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to a new activity on tap
                    Intent i = new Intent(context, ScholarshipDetails.class);
                    //i.putExtra("", Parcels.wrap(contact));
                    context.startActivity(i);
                }
            });
        }
    }
}
