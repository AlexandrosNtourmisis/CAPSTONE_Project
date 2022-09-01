package acg.edu.warningapp.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import acg.edu.warningapp.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    View view;
    TextView textView_comment, textView_commenter, textView_tm, textView_d;

    //ViewHolder for custom recyclerview view -- used for displaying the comments
    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_comment = itemView.findViewById(R.id.comment);
        textView_commenter = itemView.findViewById(R.id.commenter);
        textView_tm = itemView.findViewById(R.id.time);
        textView_d = itemView.findViewById(R.id.date);
        view = itemView;

    }
}
