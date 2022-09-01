package acg.edu.warningapp.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import acg.edu.warningapp.R;

public class CasesViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    View view;
    TextView textView_t, textView_c, textView_co, textView_tm, textView_d;

    //ViewHolder for custom recyclerview view -- used for displaying the cases
    public CasesViewHolder(@NonNull View itemView) {
        super(itemView);

        textView_t = itemView.findViewById(R.id.typeCase);
        imageView = itemView.findViewById(R.id.iconType);
        textView_c = itemView.findViewById(R.id.city);
        textView_co = itemView.findViewById(R.id.country);
        textView_tm = itemView.findViewById(R.id.time);
        textView_d = itemView.findViewById(R.id.date);
        view = itemView;

    }
}
