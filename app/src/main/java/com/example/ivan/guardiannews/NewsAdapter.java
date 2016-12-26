package com.example.ivan.guardiannews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ivan.guardiannews.data.NewsStory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ivan Lepojevic
 */

public class NewsAdapter extends ArrayAdapter<NewsStory> {

    public NewsAdapter(Context context, List<NewsStory> stories){
        super(context,0,stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Get the data for this position
        NewsStory story = getItem(position);
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        // Lookup view for data population
        holder.title.setText(story.getWebTitle());
        holder.desc.setText(story.getSectionId());
        holder.date.setText(story.getWebPublicationDate().replaceAll("[TZ]", " "));

        // Set onClickListener to open url to the story on Guardian website
        convertView.setOnClickListener(view ->
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(story.getWebUrl())))
        );

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.date)
        TextView date;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }

    }
}
