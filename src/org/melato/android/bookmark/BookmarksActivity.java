package org.melato.android.bookmark;

import java.util.ArrayList;
import java.util.List;

import org.melato.android.R;
import org.melato.android.ui.RenameFragment;
import org.melato.android.ui.RenameHandler;
import org.melato.client.Bookmark;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BookmarksActivity extends FragmentActivity implements OnItemClickListener {
  private BookmarkHandler bookmarkHandler;
  private List<Bookmark> bookmarks;
  private ListView listView;
  private ArrayAdapter<Bookmark> adapter;
  private boolean hasContextMenu = true;
  
  public BookmarksActivity(BookmarkHandler bookmarkHandler) {
    super();
    this.bookmarkHandler = bookmarkHandler;
  }

  class LoadTask extends AsyncTask<Void,Void,List<Bookmark>> {

    @Override
    protected List<Bookmark> doInBackground(Void... args) {
      List<Bookmark> bookmarks = new ArrayList<Bookmark>();
      BookmarkDatabase db = BookmarkDatabase.getInstance(BookmarksActivity.this);
      db.loadBookmarks(bookmarks);
      return bookmarks;
    }

    @Override
    protected void onPostExecute(List<Bookmark> result) {
      bookmarks = result;
      listView.setAdapter(adapter = new BookmarkAdapter());
      super.onPostExecute(result);
    }    
  }
  
  class BookmarkAdapter extends ArrayAdapter<Bookmark> {
    public BookmarkAdapter() {
      super(BookmarksActivity.this, R.layout.bookmark_list_item, R.id.text, bookmarks); 
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = super.getView(position, convertView, parent);
      TextView textView = (TextView) view.findViewById(R.id.text);
      ImageView iconView = (ImageView) view.findViewById(R.id.icon);
      Bookmark bookmark = bookmarks.get(position);
      textView.setText(bookmark.getName());
      iconView.setImageResource(bookmarkHandler.getTypeIcon(bookmark.getType()));
      return view;
    }
  }
  
  
  
  protected void setHasContextMenu(boolean hasContextMenu) {
    this.hasContextMenu = hasContextMenu;
  }
  
  public boolean getHasContextMenu() {
    return hasContextMenu;
  }

  protected void open(Bookmark bookmark) {
    bookmarkHandler.open(this, bookmark);    
  }
  @Override
  public void onItemClick(AdapterView<?> l, View view, int position, long id) {
    open(bookmarks.get(position));
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bookmarks);
    listView = (ListView) findViewById(R.id.listView);
    listView.setOnItemClickListener(this);
    if ( hasContextMenu ) {
      registerForContextMenu(listView);
    }
    new LoadTask().execute();      
  }
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
  }


  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.bookmark_menu, menu);
  }

  class BookmarkRenameHandler implements RenameHandler {
    private Bookmark bookmark;
    
    public BookmarkRenameHandler(Bookmark bookmark) {
      super();
      this.bookmark = bookmark;
    }

    @Override
    public String getName() {
      return bookmark.getName();
    }

    @Override
    public void setName(String name) {
      BookmarkDatabase.getInstance(BookmarksActivity.this).renameBookmark(bookmark,name);    
      adapter.notifyDataSetChanged();
    }
  }
  
  void renameBookmark(Bookmark bookmark, int position) {
    FragmentManager f = getSupportFragmentManager();
    RenameHandler handler = new BookmarkRenameHandler(bookmark);
    new RenameFragment(handler).show(f, "dialog");
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    if ( ! hasContextMenu )
      return false;
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    Bookmark bookmark = bookmarks.get(info.position);
    int itemId = item.getItemId();
    if ( itemId == R.id.open ) {
      open(bookmark);
      return true;
    } else if ( itemId == R.id.delete) {
      BookmarkDatabase.getInstance(this).deleteBookmark(bookmark);
      bookmarks.remove(info.position);
      adapter.notifyDataSetChanged();
      return true;
    } else if ( itemId == R.id.rename) {
      renameBookmark(bookmark, info.position);
      return true;
    } else {
      return false;
    }
  }  
}
