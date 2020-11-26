package victor.app.tirinhas.brasil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;

public class Novo extends SherlockActivity implements OnClickListener, OnMenuItemClickListener{
	
	private static final String LINK_PEEL = "market://details?id=peel.melhores.imagens.piadas.videos.engracadas.humor";

	boolean share = false;
	
	boolean visivel = false;
	
	boolean visivelM = false;
	
	boolean extras, erro, deveFicarBranca, leNinjaB = true;
	
	private android.content.SharedPreferences sp;
	
	boolean primeiraVez = true;
	
	ImageView ivMeme1, ivMeme2, ivMeme3, ivMeme4, face1, face2, face3, face4, 
	face5, face6, face7, face8, face9, face10, face11, face12, face13, 
	face14, face15, face16, face17, face18, face19, face20, face21, face22, 
	face23, face24, face25, femface1, femface2, femface3, femface4, femface5, 
	femface6, femface7, oface1, oface2, oface3, plus, faceextra1, faceextra2, 
	faceextra3, faceextra4, face26, sd;	
	
	int painel = 2;
	
	int qlopainel, qlotv, i, h, w, deleta;
	
	String gender = "masculino";
	
	HorizontalScrollView hsvMasc, hsvFem, hsvVar;
	
	Button addC, PlusText, MinusText, type;
	
	ImageButton AddPanel, RemovePanel;
	
	final Context context = this;
	
	TextView um, dois, tres, quatro, txt, marca;
	
	LinearLayout LL1, LL2, LL3, LL4, ferramentas, faces, leNinja;
	
	String fname, textoUm, textoDois, textoTres, textoQuatro, COISAS;
		
	LinearLayout RL = null;
	
	Bitmap img;
	
	Uri auri;
		
	float textSize;
	
	File file, myDir, rascunhos;
	
    final static int PHOTO_PICKED = 0;
    	
	File rsc;
	
	ScrollView svLayout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo);
        //getWindow().setFormat(PixelFormat.RGB_565);
        getSupportActionBar().setHomeButtonEnabled(true);
        SharedPreferences();
        bts();
        quadros();
    }
    
    public void criarQuadro() {
    	//Layout
    	LinearLayout llQuadro = new LinearLayout(Novo.this);
    	llQuadro.setWeightSum(100);
    	llQuadro.setPadding(dpConverter(10), dpConverter(10), dpConverter(10), dpConverter(10));
    	llQuadro.setBackgroundResource(R.drawable.caixa);
    	svLayout.addView(llQuadro, LayoutParams.MATCH_PARENT, tamanhoQuadro());
    	
    	//Imagem
    	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.lol);
		final ImageView iv = new ImageView(getApplicationContext());
		iv.setImageBitmap(bmp);
		iv.setAdjustViewBounds(true);
		iv.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 30f));
		//SetOnClickListener
		
		//Texto
		
    }
    
    public int dpConverter(int valor) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (valor*scale + 0.5f);
	}
    
    public int tamanhoQuadro() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);		
    	return (int) (metrics.heightPixels*0.4);
    }

	private void quadros() {
		// TODO Auto-generated method stub
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		h = metrics.heightPixels;
		//w = metrics.widthPixels;
		
		LayoutParams parametros = LL1.getLayoutParams();
		parametros.height = (int) (h*0.4);
		
		parametros = LL2.getLayoutParams();
		parametros.height = (int) (h*0.4);
		
		parametros = LL3.getLayoutParams();
		parametros.height = (int) (h*0.4);
		
		parametros = LL4.getLayoutParams();
		parametros.height = (int) (h*0.4);		
	}

	private void SharedPreferences() {
		// TODO Auto-generated method stub
		try {
			sp = getSharedPreferences(COISAS, MODE_PRIVATE);
			i = sp.getInt("count", 0);
			if (i < 2) {
				i++;
				Editor edit = sp.edit();
				edit.putInt("count", i);
				edit.commit();
			}
			else if (i == 2) {
				avalie();
				i++;
				Editor edit = sp.edit();
				edit.putInt("count", i);
				edit.commit();
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void avalie() {
		// TODO Auto-generated method stub
		AlertDialog.Builder InserirTXTBuilder = new AlertDialog.Builder(context);
		//Se quiser, pode setar um Title e uma message.
		InserirTXTBuilder.setTitle("Gostou?");
		InserirTXTBuilder.setCancelable(true);
		InserirTXTBuilder.setMessage("Espero que tenha gostado do app. Se puder, avalie-nos no Google Play, não demora 30s.");
		InserirTXTBuilder.setPositiveButton("Avaliar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				launchMarket();
			}
		});
		InserirTXTBuilder.setNegativeButton("Mais tarde", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog InserirTXT = InserirTXTBuilder.create();
		InserirTXT.show();
	}

	private void launchMarket() {
	    Uri uri = Uri.parse("market://details?id=" + getPackageName());
	    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
	    try {
	        startActivity(myAppLinkToMarket);
	    } catch (ActivityNotFoundException e) {
	        Toast.makeText(this, "Ops, não achamos o App", Toast.LENGTH_LONG).show();
	    }
	}
	
	private void bts() {
		// TODO Auto-generated method stub
		ivMeme1 = (ImageView) findViewById(R.id.ivMeme1);
		ivMeme1.setOnClickListener(this);
		ivMeme2 = (ImageView) findViewById(R.id.ivMeme2);
		ivMeme2.setOnClickListener(this);
		ivMeme3 = (ImageView) findViewById(R.id.ivMeme3);
		ivMeme3.setOnClickListener(this);
		ivMeme4 = (ImageView) findViewById(R.id.ivMeme4);
		ivMeme4.setOnClickListener(this);
		um = (TextView) findViewById(R.id.tvFala1);
		um.setOnClickListener(this);
		dois = (TextView) findViewById(R.id.tvFala2);
		dois.setOnClickListener(this);
		tres = (TextView) findViewById(R.id.tvFala3);
		tres.setOnClickListener(this);
		quatro = (TextView) findViewById(R.id.tvFala4);
		quatro.setOnClickListener(this);
		AddPanel = (ImageButton) findViewById(R.id.btAddPanel);
		AddPanel.setOnClickListener(this);
		RemovePanel = (ImageButton) findViewById(R.id.btRemovePanel);
		RemovePanel.setOnClickListener(this);
		LL1 = (LinearLayout) findViewById(R.id.llQuadro1);
		LL2 = (LinearLayout) findViewById(R.id.llQuadro2);
		LL3 = (LinearLayout) findViewById(R.id.llQuadro3);
		LL4 = (LinearLayout) findViewById(R.id.llQuadro4);
		PlusText = (Button) findViewById(R.id.btTextPlus);
		PlusText.setOnClickListener(this);
		MinusText = (Button) findViewById(R.id.btTextMinus);
		MinusText.setOnClickListener(this);
		ferramentas = (LinearLayout) findViewById(R.id.llFerramentas);
		faces = (LinearLayout) findViewById(R.id.llFaces);
		type = (Button) findViewById(R.id.btGender);
		type.setOnClickListener(this);
		leNinja = (LinearLayout) findViewById(R.id.llNinja);
		svLayout = (ScrollView) findViewById(R.id.scrollView1);
		facesInitializers();
	}

	private void facesInitializers() {
		// TODO Auto-generated method stub
		face1 = (ImageView) findViewById(R.id.ivFace1);
		face1.setOnClickListener(this);
		face2 = (ImageView) findViewById(R.id.ivFace2);
		face2.setOnClickListener(this);
		face3 = (ImageView) findViewById(R.id.ivFace3);
		face3.setOnClickListener(this);
		face4 = (ImageView) findViewById(R.id.ivFace4);
		face4.setOnClickListener(this);
		face5 = (ImageView) findViewById(R.id.ivFace5);
		face5.setOnClickListener(this);
		face6 = (ImageView) findViewById(R.id.ivFace6);
		face6.setOnClickListener(this);
		face7 = (ImageView) findViewById(R.id.ivFace7);
		face7.setOnClickListener(this);
		face8 = (ImageView) findViewById(R.id.ivFace8);
		face8.setOnClickListener(this);
		face9 = (ImageView) findViewById(R.id.ivFace9);
		face9.setOnClickListener(this);
		face10 = (ImageView) findViewById(R.id.ivFace10);
		face10.setOnClickListener(this);
		face11 = (ImageView) findViewById(R.id.ivFace11);
		face11.setOnClickListener(this);
		face12 = (ImageView) findViewById(R.id.ivFace12);
		face12.setOnClickListener(this);
		face13 = (ImageView) findViewById(R.id.ivFace13);
		face13.setOnClickListener(this);
		face13 = (ImageView) findViewById(R.id.ivFace13);
		face13.setOnClickListener(this);
		face14 = (ImageView) findViewById(R.id.ivFace14);
		face14.setOnClickListener(this);
		face15 = (ImageView) findViewById(R.id.ivFace15);
		face15.setOnClickListener(this);
		face16 = (ImageView) findViewById(R.id.ivFace16);
		face16.setOnClickListener(this);
		face17 = (ImageView) findViewById(R.id.ivFace17);
		face17.setOnClickListener(this);
		face18 = (ImageView) findViewById(R.id.ivFace18);
		face18.setOnClickListener(this);
		face19 = (ImageView) findViewById(R.id.ivFace19);
		face19.setOnClickListener(this);
		face20 = (ImageView) findViewById(R.id.ivFace20);
		face20.setOnClickListener(this);
		face21 = (ImageView) findViewById(R.id.ivFace21);
		face21.setOnClickListener(this);
		face22 = (ImageView) findViewById(R.id.ivFace22);
		face22.setOnClickListener(this);
		face23 = (ImageView) findViewById(R.id.ivFace23);
		face23.setOnClickListener(this);
		face24 = (ImageView) findViewById(R.id.ivFace24);
		face24.setOnClickListener(this);
		face25 = (ImageView) findViewById(R.id.ivFace25);
		face25.setOnClickListener(this);
		femface1 = (ImageView) findViewById(R.id.ivFaceFeminino1);
		femface1.setOnClickListener(this);
		femface2 = (ImageView) findViewById(R.id.ivFaceFeminino2);
		femface2.setOnClickListener(this);
		femface3 = (ImageView) findViewById(R.id.ivFaceFeminino3);
		femface3.setOnClickListener(this);
		femface4 = (ImageView) findViewById(R.id.ivFaceFeminino4);
		femface4.setOnClickListener(this);
		femface5 = (ImageView) findViewById(R.id.ivFaceFeminino5);
		femface5.setOnClickListener(this);
		femface6 = (ImageView) findViewById(R.id.ivFaceFeminino6);
		femface6.setOnClickListener(this);
		femface7 = (ImageView) findViewById(R.id.ivFaceFeminino7);
		femface7.setOnClickListener(this);
		oface1 = (ImageView) findViewById(R.id.ivFaceVariados1);
		oface1.setOnClickListener(this);
		oface2 = (ImageView) findViewById(R.id.ivFaceVariados2);
		oface2.setOnClickListener(this);
		plus = (ImageView) findViewById(R.id.ivFaceVariados3);
		plus.setOnClickListener(this);
		faceextra1 = (ImageView) findViewById(R.id.ivFaceVariados4);
		faceextra1.setOnClickListener(this);
		faceextra2 = (ImageView) findViewById(R.id.ivFaceVariados5);
		faceextra2.setOnClickListener(this);
		faceextra3 = (ImageView) findViewById(R.id.ivFaceVariados6);
		faceextra3.setOnClickListener(this);
		faceextra4 = (ImageView) findViewById(R.id.ivFaceVariados7);
		faceextra4.setOnClickListener(this);
		hsvMasc = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		hsvFem = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewFeminino);
		hsvVar = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewVariados);
		verificaExtra();
		face26 = (ImageView) findViewById(R.id.ivFace26);
		face26.setOnClickListener(this);
		sd = (ImageView) findViewById(R.id.ivFaceVariados0);
		sd.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		String hue = item.getTitle().toString();
		System.out.println(hue);
		if (hue.equals("+")) {
			//Setar elas visíveis/invisíveis.
			ferramentasV();
		}
    	else if (hue.equals("SALVAR")) {
    		if (primeiraVez == true) {
    			textoUm = um.getText().toString();
    			textoDois = dois.getText().toString();
    			textoTres = tres.getText().toString();
    			textoQuatro = quatro.getText().toString();
    			primeiraVez = false;
    			share = false;
        		new OutraTask().execute();
        		//Toast.makeText(getApplicationContext(), "Sua imagem foi salva", Toast.LENGTH_SHORT).show();
    		}
    		else {
    			if (textoUm.equals(um.getText().toString()) && textoDois.equals(dois.getText().toString()) && textoTres.equals(tres.getText().toString()) && textoQuatro.equals(quatro.getText().toString())) {
    				Toast.makeText(getApplicationContext(), "Imagem já foi salva!", Toast.LENGTH_LONG).show();
    			}
    			else {
    				textoUm = um.getText().toString();
        			textoDois = dois.getText().toString();
        			textoTres = tres.getText().toString();
        			textoQuatro = quatro.getText().toString();
        			share = false;
            		new OutraTask().execute();
            		//Toast.makeText(getApplicationContext(), "Sua imagem foi salva", Toast.LENGTH_SHORT).show();
    			}
    		}
    	}
    	else if (hue.equals("Compartilhar")) {
    		if (primeiraVez == true) {
    			textoUm = um.getText().toString();
    			textoDois = dois.getText().toString();
    			textoTres = tres.getText().toString();
    			textoQuatro = quatro.getText().toString();
    			primeiraVez = false;
    			share = true;
        		new OutraTask().execute();
        		//compartilhe();
    		}
    		else {
    			if (textoUm.equals(um.getText().toString()) && textoDois.equals(dois.getText().toString()) && textoTres.equals(tres.getText().toString()) && textoQuatro.equals(quatro.getText().toString())) {
    				//Toast.makeText(getApplicationContext(), "Imagem já foi salva!", Toast.LENGTH_LONG).show();
    				compartilhe();
    			}
    			else {
    				textoUm = um.getText().toString();
        			textoDois = dois.getText().toString();
        			textoTres = tres.getText().toString();
        			textoQuatro = quatro.getText().toString();
        			share = true;
            		new OutraTask().execute();
            		//compartilhe();
    			}
    		}
    	}
    	else if (hue.equals("Sobre")) {
    		Intent i = new Intent(Novo.this, Sobre.class);
    		startActivity(i);
    	}
		return false;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(Novo.this, Sobre.class);
    		startActivity(i);
    		break;			
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		//TEXTOS
		
		case R.id.tvFala1:
			qlotv = 1;
			poptxt();
			break;
		
		case R.id.tvFala2:
			qlotv = 2;
			poptxt();
			break;
			
		case R.id.tvFala3:
			qlotv = 3;
			poptxt();
			break;
			
		case R.id.tvFala4:
			qlotv = 4;
			poptxt();
			break;
			
			//FERRAMENTAS
			
		case R.id.btAddPanel:
			if (painel == 2) {
				LL3.setVisibility(View.VISIBLE);
				painel++;
			}
			else if(painel == 3) {
				LL4.setVisibility(View.VISIBLE);
				painel++;
			}
			else if (painel == 4) {
				Toast.makeText(getApplicationContext(), "Você chegou ao limite!", Toast.LENGTH_SHORT).show();
			}
			else if (painel == 1) {
				LL2.setVisibility(View.VISIBLE);
				painel++;
			}
			break;
			
		case R.id.btRemovePanel:
			if (painel == 2) {
				LL2.setVisibility(View.GONE);
				painel--;
			}
			else if(painel == 3) {
				LL3.setVisibility(View.GONE);
				painel--;
			}
			else if (painel == 4) {
				LL4.setVisibility(View.GONE);
				painel--;
			}
			else if (painel == 1) {
				Toast.makeText(getApplicationContext(), "Você precisa de pelo menos 1", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btTextPlus:
			textSize = um.getTextSize();
			textSize++;
			um.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			dois.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			tres.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			quatro.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			break;
			
		case R.id.btTextMinus:
			textSize = um.getTextSize();
			textSize--;
			um.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			dois.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			tres.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			quatro.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			break;
			
			//PAINÉIS
			
		case R.id.ivMeme1:
			qlopainel = 1;
			deveFicarBranca = false;
			checaVisibilidade();
			break;
			
		case R.id.ivMeme2:
			qlopainel = 2;
			deveFicarBranca = false;
			checaVisibilidade();
			break;
			
		case R.id.ivMeme3:
			qlopainel = 3;
			deveFicarBranca = false;
			checaVisibilidade();
			break;
			
		case R.id.ivMeme4:
			qlopainel = 4;
			deveFicarBranca = false;
			checaVisibilidade();
			break;
			
			//Seleção de faces:
			
		case R.id.btGender:
			faces();
			break;
			
		case R.id.ivFace1:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.normal);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.normal);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.normal);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.normal);
			}
			checaVisibilidade();
			break;

		case R.id.ivFace2:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.bitchplease);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.bitchplease);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.bitchplease);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.bitchplease);
			}
			checaVisibilidade();
			break;

		case R.id.ivFace3:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.foreveralone);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.foreveralone);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.foreveralone);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.foreveralone);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace4:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.fuckyeah);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.fuckyeah);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.fuckyeah);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.fuckyeah);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace5:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.iseewhatyoudidthere);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.iseewhatyoudidthere);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.iseewhatyoudidthere);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.iseewhatyoudidthere);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace6:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.megusta);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.megusta);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.megusta);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.megusta);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace7:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.trollface);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.trollface);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.trollface);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.trollface);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace8:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.areyoukiddingme);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.areyoukiddingme);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.areyoukiddingme);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.areyoukiddingme);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace9:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.areyouserious);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.areyouserious);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.areyouserious);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.areyouserious);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace10:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.cuteness);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.cuteness);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.cuteness);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.cuteness);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace11:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.genius);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.genius);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.genius);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.genius);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace12:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.likeasir);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.likeasir);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.likeasir);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.likeasir);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace13:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.no);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.no);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.no);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.no);
			}
			checaVisibilidade();
			break;			
			
		case R.id.ivFace14:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.fliptable);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.fliptable);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.fliptable);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.fliptable);
			}
			checaVisibilidade();
			break;

		case R.id.ivFace15:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.seriouspc);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.seriouspc);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.seriouspc);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.seriouspc);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace16:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.fuumale);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.fuumale);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.fuumale);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.fuumale);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace17:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.sadface);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.sadface);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.sadface);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.sadface);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace18:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.hihihi);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.hihihi);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.hihihi);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.hihihi);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace19:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.meh);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.meh);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.meh);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.meh);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace20:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.hehehe);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.hehehe);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.hehehe);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.hehehe);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace21:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.youdontsay);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.youdontsay);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.youdontsay);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.youdontsay);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace22:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.credo);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.credo);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.credo);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.credo);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace23:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.yunoface);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.yunoface);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.yunoface);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.yunoface);
			}
			checaVisibilidade();
			break;
		
		case R.id.ivFace24:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.sheknows);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.sheknows);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.sheknows);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.sheknows);
			}
			deveFicarBranca = true;
			checaVisibilidade();
			break;
		
		case R.id.ivFace25:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.whats);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.whats);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.whats);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.whats);
			}
			deveFicarBranca = true;
			checaVisibilidade();
			break;
			
		case R.id.ivFace26:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.lol);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.lol);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.lol);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.lol);
			}
			checaVisibilidade();
			break;
			
			//FACES FEMININAS
			
		case R.id.ivFaceFeminino1:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.derpina);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.derpina);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.derpina);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.derpina);
			}
			checaVisibilidade();
			break;

		case R.id.ivFaceFeminino2:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.female);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.female);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.female);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.female);
			}
			checaVisibilidade();
			break;

		case R.id.ivFaceFeminino3:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.fuufemale);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.fuufemale);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.fuufemale);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.fuufemale);
			}
			checaVisibilidade();
			break;

		case R.id.ivFaceFeminino4:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.megustablonde);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.megustablonde);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.megustablonde);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.megustablonde);
			}
			checaVisibilidade();
			break;

		case R.id.ivFaceFeminino5:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.femaleblackgusta);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.femaleblackgusta);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.femaleblackgusta);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.femaleblackgusta);
			}
			checaVisibilidade();
			break;

		case R.id.ivFaceFeminino6:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.trollfemale);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.trollfemale);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.trollfemale);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.trollfemale);
			}
			checaVisibilidade();
			break;

		case R.id.ivFaceFeminino7:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.girlglasses);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.girlglasses);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.girlglasses);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.girlglasses);
			}
			checaVisibilidade();
			break;
			
			//FACES VARIADAS
			
		case R.id.ivFaceVariados1:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.dolan);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.dolan);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.dolan);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.dolan);
			}
			checaVisibilidade();
			break;
			
		case R.id.ivFaceVariados2:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.gooby);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.gooby);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.gooby);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.gooby);
			}
			checaVisibilidade();
			break;
			
		case R.id.ivFaceVariados3:
			facebook();
			checaVisibilidade();
			break;
			
		case R.id.ivFaceVariados4:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.cereal1);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.cereal1);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.cereal1);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.cereal1);
			}
			checaVisibilidade();
			break;
			
		case R.id.ivFaceVariados5:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.cereal2);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.cereal2);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.cereal2);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.cereal2);
			}
			checaVisibilidade();
			break;
			
		case R.id.ivFaceVariados6:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.jornal);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.jornal);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.jornal);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.jornal);
			}
			checaVisibilidade();
			break;
			
		case R.id.ivFaceVariados7:
			if (qlopainel == 1) {
				ivMeme1.setImageResource(R.drawable.jornalrasgo);
			}
			else if (qlopainel == 2) {
				ivMeme2.setImageResource(R.drawable.jornalrasgo);
			}
			else if (qlopainel == 3) {
				ivMeme3.setImageResource(R.drawable.jornalrasgo);
			}
			else if (qlopainel == 4) {
				ivMeme4.setImageResource(R.drawable.jornalrasgo);
			}
			checaVisibilidade();
			break;
			
		case R.id.ivFaceVariados0:
			selectContent();
			break;
		}
	}

	private void verificaExtra() {
		//Editado. Livre.
		plus.setVisibility(View.GONE);
		faceextra1.setVisibility(View.VISIBLE);
		faceextra2.setVisibility(View.VISIBLE);
		faceextra3.setVisibility(View.VISIBLE);
		faceextra4.setVisibility(View.VISIBLE);
		try {
			sp = getSharedPreferences(COISAS, MODE_PRIVATE);
			leNinjaB = sp.getBoolean("exibePeel", true);
			if (!leNinjaB) {
				leNinja.setVisibility(View.GONE);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		/*
		try {
			sp = getSharedPreferences(COISAS, MODE_PRIVATE);
			extras = sp.getBoolean("extras", false);
			if (extras==true) {
				plus.setVisibility(View.GONE);
				faceextra1.setVisibility(View.VISIBLE);
				faceextra2.setVisibility(View.VISIBLE);
				faceextra3.setVisibility(View.VISIBLE);
				faceextra4.setVisibility(View.VISIBLE);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
	private void facebook() {
		// TODO Auto-generated method stub
		AlertDialog.Builder InserirTXTBuilder = new AlertDialog.Builder(context);
		InserirTXTBuilder.setTitle("Curta-nos");
		InserirTXTBuilder.setCancelable(true);
		InserirTXTBuilder.setMessage("Curta nossa página no Facebook para ficar por dentro de novidades sobre esses e outros aplicativos, e libere 4 novos memes!");
		InserirTXTBuilder.setPositiveButton("Curtir", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Obrigado", Toast.LENGTH_SHORT).show();
				Intent fb = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/AppsAleatorios"));
				startActivity(fb);
				try {
					sp = getSharedPreferences(COISAS, MODE_PRIVATE);
					extras = sp.getBoolean("extras", false);
					Editor edit = sp.edit();
					edit.putBoolean("extras", true);
					edit.commit();
					plus.setVisibility(View.GONE);
					faceextra1.setVisibility(View.VISIBLE);
					faceextra2.setVisibility(View.VISIBLE);
					faceextra3.setVisibility(View.VISIBLE);
					faceextra4.setVisibility(View.VISIBLE);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		InserirTXTBuilder.setNegativeButton("Mais tarde", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog InserirTXT = InserirTXTBuilder.create();
		InserirTXT.show();
	}

	private void faces() {
		// TODO Auto-generated method stub
		if (gender.equals("masculino")) {
			gender = "feminino";
			hsvMasc.setVisibility(View.GONE);
			hsvFem.setVisibility(View.VISIBLE);
			type.setText("Mulheres");
		}
		else if (gender.equals("feminino")) {
			gender = "variados";
			hsvFem.setVisibility(View.GONE);
			hsvVar.setVisibility(View.VISIBLE);
			type.setText("Outros");
		}
		else if (gender.equals("variados")) {
			gender = "masculino";
			hsvVar.setVisibility(View.GONE);
			hsvMasc.setVisibility(View.VISIBLE);
			type.setText("Homens");
		}
	}
	
	private void defineCor() {
		switch (qlopainel) {
		case 1:
			um.setTextColor(Color.WHITE);
			LL1.setBackgroundColor(Color.BLACK);
			break;
		
		case 2:
			dois.setTextColor(Color.WHITE);
			LL2.setBackgroundColor(Color.BLACK);
			break;
		
		case 3:
			tres.setTextColor(Color.WHITE);
			LL3.setBackgroundColor(Color.BLACK);
			break;
		
		case 4:
			quatro.setTextColor(Color.WHITE);
			LL4.setBackgroundColor(Color.BLACK);
			break;
			}
		deveFicarBranca = false;
	}
	
	private void preto() {
		switch (qlopainel) {
		case 1:
			um.setTextColor(Color.BLACK);
			LL1.setBackgroundResource(R.drawable.caixa);
			break;
		
		case 2:
			dois.setTextColor(Color.BLACK);
			LL2.setBackgroundResource(R.drawable.caixa);
			break;
		
		case 3:
			tres.setTextColor(Color.BLACK);
			LL3.setBackgroundResource(R.drawable.caixa);
			break;
		
		case 4:
			quatro.setTextColor(Color.BLACK);
			LL4.setBackgroundResource(R.drawable.caixa);
			break;
			}
	}
	
	private void cor() {
		System.out.println(deveFicarBranca);
		if (deveFicarBranca){
			defineCor();
		}
		else if (!deveFicarBranca) {
			preto();
		}
		else {
			deveFicarBranca = false;
		}
	}
	
	private void checaVisibilidade() {
		// TODO Auto-generated method stub
		if (visivelM) {
			faces.setVisibility(View.GONE);
			visivelM = false;				
		}
		else if (!visivelM) {
			faces.setVisibility(View.VISIBLE);
			ferramentas.setVisibility(View.GONE);
			visivelM = true;	
		}
			
		cor ();
				
	}
	
	private void salva() {
		nomeDoArquivo();
		if(myDir.exists()==false) {
            myDir.mkdirs();
		}
		try {
			RL = (LinearLayout) findViewById(R.id.llQuadro1);
			RL.setDrawingCacheEnabled(true);
			Bitmap screenshot;
			DisplayMetrics metrics = new DisplayMetrics();    
			getWindowManager().getDefaultDisplay().getMetrics(metrics);    
			int screenDensity = metrics.densityDpi;
			System.out.println(screenDensity);
			screenshot = Bitmap.createBitmap(RL.getDrawingCache());
			Canvas canvas = new Canvas(screenshot);
			file.createNewFile();
	        FileOutputStream out = new FileOutputStream(file);
			canvas.drawBitmap(screenshot, 0, 0, null);
			screenshot.compress(CompressFormat.JPEG, 100, out);
			out.flush();
	        out.close();
	        RL.setDrawingCacheEnabled(false);	
	        erro = false;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERRO");
			erro = true;
		}
		finally {
			
		}
		//Atualiza.
		sendBroadcast(new Intent(
	    	    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
	    	    Uri.parse("file://"+Environment.getExternalStorageDirectory()+"/TirinhasBrasil/"+fname)));
	}
	
	private void criarRascunhos() {
		int tira = 1;

		rascunhos = new File(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/");
				
		if (rascunhos.exists() == false) {
            rascunhos.mkdirs();
		}
		
		while (tira <= painel) {
			try {

				fname = "Tirinha_Rascunho_Quadro_"+tira+".jpg";
				
				file = new File (rascunhos, fname);

				LinearLayout a = null;
				
				if (tira==1)
					a = (LinearLayout) findViewById(R.id.llQuadro1);
				else if (tira==2)
					a = (LinearLayout) findViewById(R.id.llQuadro2);
				else if (tira==3)
					a = (LinearLayout) findViewById(R.id.llQuadro3);
				else if (tira==4)
					a = (LinearLayout) findViewById(R.id.llQuadro4);
				a.setDrawingCacheEnabled(true);
				Bitmap screenshot;
				screenshot = Bitmap.createBitmap(a.getDrawingCache());
				Canvas canvas = new Canvas(screenshot);
				file.createNewFile();
		        FileOutputStream out = new FileOutputStream(file);
				canvas.drawBitmap(screenshot, 0, 0, null);
				screenshot.compress(CompressFormat.JPEG, 100, out);
				out.flush();
		        out.close();
		        a.setDrawingCacheEnabled(false);	
		        erro = false;
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERRO");
				erro = true;
			}
		tira++;
		}
		
		juntaTudo();
	}
	
	private void juntaTudo() {
		Bitmap q1 = null;
		Bitmap q2 = null;
		Bitmap q3 = null;
		Bitmap q4 = null;
		switch (painel) {
		case 1:
			q1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_1.jpg");
			break;
			
		case 2:
			q1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_1.jpg");
			q2 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_2.jpg");
			break;
			
		case 3:
			q1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_1.jpg");
			q2 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_2.jpg");
			q3 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_3.jpg");
			break;
			
		case 4:
			q1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_1.jpg");
			q2 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_2.jpg");
			q3 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_3.jpg");
			q4 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/Rascunhos_TDM/Tirinha_Rascunho_Quadro_4.jpg");
			break;
		}
		
		int alt = q1.getHeight();
		int larg = q1.getWidth();
		alt = alt*painel;

		Bitmap cs = null;
			
		cs = Bitmap.createBitmap(larg, alt, Bitmap.Config.ARGB_8888); 
			 
		Canvas comboImage = new Canvas(cs); 
		 
		switch (painel) {
		case 1:
			comboImage.drawBitmap(q1, 0f, 0f, null); 
			break;
			
		case 2:
			comboImage.drawBitmap(q1, 0f, 0f, null); 
			comboImage.drawBitmap(q2, 0f, q1.getHeight(), null); 
			break;
			
		case 3:
			comboImage.drawBitmap(q1, 0f, 0f, null); 
			comboImage.drawBitmap(q2, 0f, q1.getHeight(), null); 
			comboImage.drawBitmap(q3, 0f, q1.getHeight()*2, null);
			break;
			
		case 4:
			comboImage.drawBitmap(q1, 0f, 0f, null); 
			comboImage.drawBitmap(q2, 0f, q1.getHeight(), null); 
			comboImage.drawBitmap(q3, 0f, q1.getHeight()*2, null);
			comboImage.drawBitmap(q4, 0f, q1.getHeight()*3, null);
			break;
		}
		
		nomeDoArquivo();
		
		if (myDir.exists() == false) {
			myDir.mkdir();
		}
		
		FileOutputStream out;
		
		try {
			out = new FileOutputStream(file);
			cs.compress(CompressFormat.JPEG, 100, out);
			out.flush();
	        out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			erro = true;
		}
		
		sendBroadcast(new Intent(
	    	    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
	    	    Uri.parse("file://"+Environment.getExternalStorageDirectory()+"/TirinhasBrasil/"+fname)));
		
		deleta = 1;
		
		while (deleta <= painel) {
			rsc = new File(rascunhos, "Tirinha_Rascunho_Quadro_"+deleta+".jpg");
			rsc.delete();
			deleta++;
		}
		
		rascunhos.delete();
		
	}
	
	private void poptxt() {
		LayoutInflater infle = getLayoutInflater();
		View txtlayout = infle.inflate(R.layout.poptxt, null);
		
		AlertDialog.Builder InserirTXTBuilder = new AlertDialog.Builder(context);
		//Se quiser, pode setar um Title e uma message.
		InserirTXTBuilder.setView(txtlayout);
		final EditText txt = (EditText) txtlayout.findViewById(R.id.et1);
		switch(qlotv) {
			case 1:
				txt.setText(um.getText());
				break;
				
			case 2:
				txt.setText(dois.getText());
				break;
				
			case 3:
				txt.setText(tres.getText());
				break;
				
			case 4:
				txt.setText(quatro.getText());
				break;
		}
		InserirTXTBuilder.setCancelable(true);
		InserirTXTBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch(qlotv) {
				case 1:
					um.setText(txt.getText());
					break;
					
				case 2:
					dois.setText(txt.getText());
					break;
					
				case 3:
					tres.setText(txt.getText());
					break;
					
				case 4:
					quatro.setText(txt.getText());
					break;
				}	
			}
		});
		InserirTXTBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog InserirTXT = InserirTXTBuilder.create();
		InserirTXT.show();
	}
	
	private void compartilhe() {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues(2);
	    values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
	    values.put(MediaStore.Images.Media.DATA, Environment.getExternalStorageDirectory().getPath()+"/TirinhasBrasil/"+fname);
	    auri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	    Intent nick = new Intent();
        nick.setAction(Intent.ACTION_SEND);
        nick.setType("image/*");
        nick.putExtra(Intent.EXTRA_STREAM, auri);
        nick.putExtra(Intent.EXTRA_TEXT, "Fiz uma imagem com o #AppTirinhasBrasil");
        try {
            startActivity(nick);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show();
        }
	}
	
	private void nomeDoArquivo() {
		//Nomeando o arquivo:
		Random generator = new Random();
		int x = generator.nextInt(1000000000);
		int y = generator.nextInt(1000000000);
		SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
		String currentDateandTime = sdf.format(new Date());
		fname = "Tirinha_"+ currentDateandTime + "_" + x +"_"+ y +".jpg";
		myDir = new File(Environment.getExternalStorageDirectory().getPath()+"/TirinhasBrasil/");
		file = new File (myDir, fname);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	menu.add("Sobre")
    	.setOnMenuItemClickListener(this)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	
        menu.add("+")
        .setIcon(android.R.drawable.ic_menu_manage)
        .setOnMenuItemClickListener(this)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        menu.add("SALVAR")
        	.setIcon(android.R.drawable.ic_menu_save)
            .setOnMenuItemClickListener(this)
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        menu.add("Compartilhar")
        .setOnMenuItemClickListener(this)
    	.setIcon(android.R.drawable.ic_menu_share)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        return true;
    }
    
    private void ferramentasV() {
    	if (!visivel) {
    		ferramentas.setVisibility(View.VISIBLE);
    		visivel = true;
    	}
    	else if (visivel) {
    		ferramentas.setVisibility(View.GONE);
    		visivel = false;
    	}
    }
    
    public void onBackPressed() {
    	if (visivel || visivelM) {
    		ferramentas.setVisibility(View.GONE);
    		visivel = false;
    		faces.setVisibility(View.GONE);
			visivelM = false;	
    	}
    	else {
    		popsai();
    	}
    }
    
    private void popsai() {		
		AlertDialog.Builder InserirTXTBuilder = new AlertDialog.Builder(context);
		//Se quiser, pode setar um Title e uma message.
		InserirTXTBuilder.setTitle("Quer sair?");
		InserirTXTBuilder.setCancelable(true);
		InserirTXTBuilder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		InserirTXTBuilder.setNegativeButton("Ficar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog InserirTXT = InserirTXTBuilder.create();
		InserirTXT.show();
	}
    
	public class OutraTask extends AsyncTask<String, Void, Boolean> {

		private static final String msg1 = "Salvando a imagem...";
				
		ProgressDialog pd;
		
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				//salva();
				criarRascunhos();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			if (pd.isShowing())
				pd.dismiss();
			if (share==true) {
				compartilhe();
			}
			if (erro==true) {
				Toast.makeText(getApplicationContext(), "Seu aparelho não é compatível com esse app", Toast.LENGTH_SHORT).show();
				//ABRIR TELA DE EXPLICAÇÕES
			}
			else {
				Toast.makeText(getApplicationContext(), "Imagem salva na pasta TirinhasBrasil", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(Novo.this, "", msg1, true);
			pd.show();
		}
		
	}

	private void selectContent() {
		// TODO Auto-generated method stub
		Intent content = new Intent();
		content.setAction(Intent.ACTION_GET_CONTENT);
		content.setType("image/*");
		startActivityForResult(content, PHOTO_PICKED);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != RESULT_OK) {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
		}
		
		if (resultCode == RESULT_OK) {
			if (data == null) {
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
			}
			
			if (data.getData() == null) {
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
			}
			try {
				Bitmap bmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
				switch(qlopainel) {
				case 1:
					ivMeme1.setImageBitmap(bmap);
					break;
				
				case 2:
					ivMeme2.setImageBitmap(bmap);
					break;
					
				case 3:
					ivMeme3.setImageBitmap(bmap);
					break;
					
				case 4:
					ivMeme4.setImageBitmap(bmap);
					break;
				}
				
				checaVisibilidade();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void popUpLeNinja(View v) {
		try {
			sp = getSharedPreferences(COISAS, MODE_PRIVATE);
			extras = sp.getBoolean("exibePeel", true);
			Editor edit = sp.edit();
			edit.putBoolean("exibePeel", false);
			edit.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		leNinja.setVisibility(View.GONE);
		AlertDialog.Builder InserirTXTBuilder = new AlertDialog.Builder(context);
		//Se quiser, pode setar um Title e uma message.
		InserirTXTBuilder.setTitle("Compartilhe suas tirinhas!");
		InserirTXTBuilder.setCancelable(false);
		InserirTXTBuilder.setMessage("Peel é um app feito para compartilhar as imagens e videos mais engraçados da internet no seu celular. Compartilhe sua tirinha lá!");
		InserirTXTBuilder.setPositiveButton("Baixar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			    Uri uri = Uri.parse(LINK_PEEL);
			    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
			    try {
			        startActivity(myAppLinkToMarket);
			    } catch (ActivityNotFoundException e) {
			        Toast.makeText(Novo.this, "Ops, não achamos o App", Toast.LENGTH_LONG).show();
			    }
			}
		});
		InserirTXTBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog InserirTXT = InserirTXTBuilder.create();
		InserirTXT.show();
	}

}