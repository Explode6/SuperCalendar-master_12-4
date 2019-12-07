package com.hqyxjy.ldf.supercalendar;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View view,int position,String content);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<Event> eventList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView inputContent;
        LinearLayout itemLayout;
        public ViewHolder(View view){
            super(view);
            inputContent = (TextView)view.findViewById(R.id.item_content);
            itemLayout = (LinearLayout)view.findViewById(R.id.item_layout);
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }
    public EventAdapter(List<Event>eventList){
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Event event = eventList.get(position);
                String content = event.getEventContent();
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(holder.itemLayout,position,content);
                }
            }
        });
        holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                onItemLongClickListener.onItemLongClick(holder.itemLayout,position);
                return false;
            }
        });
        Event event = eventList.get(position);
        holder.inputContent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        holder.inputContent.getPaint().setAntiAlias(true);
        holder.inputContent.setText(event.getEventContent());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
