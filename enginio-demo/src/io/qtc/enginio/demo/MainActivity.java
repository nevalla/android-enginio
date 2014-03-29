package io.qtc.enginio.demo;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import io.qtc.enginio.EnginioCollection;
import io.qtc.enginio.EnginioHttpResponseHandler;
import io.qtc.enginio.Enginio;
import io.qtc.enginio.demo.R;

import java.util.ArrayList;
import org.json.JSONObject;

public class MainActivity extends ListActivity {
	final static String BACKEND_ID = "53031bc75a3d8b0c29033aca"; //"Your Backend id here";
	ArrayList<Object> todos;
	TodosAdapter adapter;
	Context mContext;
	Enginio eds;
	EnginioCollection todosCollection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.i("EnginioDemo", "Starting APP");
        eds = new Enginio(BACKEND_ID);
		todosCollection = eds.getCollection("todos");
		
        todos = new ArrayList<Object>();
        adapter = new TodosAdapter(this, R.layout.todo_list_item, todos);
       
        getListView().setAdapter(adapter);
        registerForContextMenu(getListView());
        mContext = this;
      
    	refreshTodos();
    
    }
    
    private void refreshTodos() {
    	findViewById(R.id.loading).setVisibility(View.VISIBLE);
    	
    	if(todosCollection != null) {
	    	todosCollection.find(new EnginioHttpResponseHandler(eds, Todo.class){
	        	@Override
	        	public void onSuccess(int statusCode, ArrayList<Object> response) {
	        		todos.clear();
	        		findViewById(R.id.loading).setVisibility(View.GONE);
	        		
	        		for (Object object : response) {
						todos.add(object);
						adapter.notifyDataSetChanged();
					}
	        	}
	        	@Override
	        	public void onFailure(int statusCode, Throwable e,
	        			JSONObject errorResponse) {
	        		Log.i("Enginio failure", errorResponse.toString());
	        	}
	        });
    	}
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.action_refresh:
    			refreshTodos();
    			return true;
		    case R.id.action_new:
				final EditText input = new EditText(mContext);
				final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				new AlertDialog.Builder(mContext)
			    .setTitle("New Todo")
			    .setView(input)
			    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            Editable value = input.getText();
						try {
				            final Todo todo = new Todo(eds);
				            todo.put("title", value.toString());
				            todo.save(new EnginioHttpResponseHandler(eds) {
				            	@Override
				            	public void onSuccess(int statusCode,
				            			Object response) {
				            		todos.add(todo);
				            		adapter.notifyDataSetChanged();
				            	}
				            });
						} catch (Exception e) {
							e.printStackTrace();
						}
			        }
			    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            // Do nothing.
			        }
			    }).show().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				
				input.requestFocus();
				
	    
				return true;
			default:
				return super.onOptionsItemSelected(item);
    	}
		
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();	
    	final int position = info.position;
    	switch(item.getItemId()) {
    		case R.id.action_delete:    			
    			Todo todo = (Todo)todos.get(position);
    			final String title = todo.getString("title");
    			
    			todo.delete(new EnginioHttpResponseHandler(eds) {
    				@Override
    				public void onSuccess(int statusCode, Object object) {
    					Toast.makeText(mContext, "Todo '"+title+"' deleted", Toast.LENGTH_SHORT).show();
    					todos.remove(position);
    					adapter.notifyDataSetChanged();
    				}
    			});
    			
    			return true;
	    	default:
	    		return super.onContextItemSelected(item);
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class TodosAdapter extends BaseAdapter {
    	Context context;
    	ArrayList<Object> todos;
    	int textViewResourceId;
    	
        public TodosAdapter(Context context, int textViewResourceId,
            ArrayList<Object> objects) {
        	this.context = context;
        	this.textViewResourceId = textViewResourceId;
        	todos = objects;
         
        }
        
     // create a new view for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
        	final CheckedTextView title;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
            	LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(textViewResourceId, null);
            	
            }
            title = (CheckedTextView)v.findViewById(R.id.title);
            
            Todo todo = (Todo)todos.get(position);
            if(todo.getBoolean("completed", false)) {
            	title.setChecked(true);
				title.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
				title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}
			else {
				title.setChecked(false);
				title.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
				title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
			}
            
            title.setText(todo.getString("title"));	
            
            return v;
        }
        
            
        
        public int getCount() {
            return todos.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
          return true;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View view, int position, long id) {
    	final CheckedTextView title = (CheckedTextView)view.findViewById(R.id.title);
		
		final Todo todo = (Todo)todos.get(position);
		
		boolean completed;
		final boolean oldValue = todo.getBoolean("completed", false);
		
		title.toggle();
		if(title.isChecked()) {
			title.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
			title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			completed = true;
		}
		else {
			title.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
			title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
			completed = false;
		}
		todo.put("completed", completed);
		
		todo.save(new EnginioHttpResponseHandler(eds) {
			@Override
			public void onFailure(int statusCode, Throwable e,
					JSONObject errorResponse) {
				Toast.makeText(mContext, "Todo '"+todo.getString("title")+"' failed. "+errorResponse.toString(), Toast.LENGTH_SHORT).show();
				title.setChecked(oldValue);
			
			}
			
			@Override
			public void onSuccess(int statusCode, Object object) {
				Toast.makeText(mContext, "Todo '"+todo.getString("title")+"' saved", Toast.LENGTH_SHORT).show();
			}
		});
    }
    
}
