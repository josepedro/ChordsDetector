package br.josepedro.chordsdetector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.jp.example.soundrecordingexample2.R;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static final float minEnergy = (float) 0.07;

	private Menu optionsMenu;

	private MyFFT myfft;
	private TextView chordTV;
	private ImageButton btnSetSoundGetChord;
	private RecordingAndSetChord asyncTaskChords = null;
	private boolean started = false;

	private ImageButton btnHelp;

	private AudioRecord recorder = null;
	private int bufferSize = 0;

	 private ProgressBar pbC;
	 private ProgressBar pbCs;
	 private ProgressBar pbD;
	 private ProgressBar pbEb;
	 private ProgressBar pbE;
	 private ProgressBar pbF;
	 private ProgressBar pbFs;
	 private ProgressBar pbG;
	 private ProgressBar pbGs;
	 private ProgressBar pbA;
	 private ProgressBar pbBb;
	 private ProgressBar pbB;
	 private ProgressBar[] barraCores;

	private ProgressBar pbEnergy;

	private Drawable microphoneButton;

	private Shader textGradient;

	// private static String[] notas = { "C", "C#", "D", "Eb", "E", "F", "F#",
	// "G", "G#", "A", "Bb", "B" };
	private static int[] coresNotas = { Color.rgb(95, 158, 160),
			Color.rgb(218, 165, 32), Color.rgb(0, 0, 255),
			Color.rgb(50, 205, 50), Color.rgb(123, 104, 238),
			Color.rgb(255, 20, 147), Color.rgb(238, 130, 238),
			Color.rgb(255, 0, 0), Color.rgb(165, 42, 42),
			Color.rgb(255, 165, 0), Color.rgb(205, 133, 63),
			Color.rgb(0, 206, 209) };

	private static String[] nomeAcordes = { "F", "F m", "F aum", "F dim", "F#",
			"F# m", "F# aum", "F# dim", "G", "G m", "G aum", "G dim", "G#",
			"G# m", "G# aum", "G# dim", "A", "A m", "A aum", "A dim", "Bb",
			"Bb m", "Bb aum", "Bb dim", "B", "B m", "B aum", "B dim", "C",
			"C m", "C aum", "C dim", "C#", "C# m", "C# aum", "C# dim", "D",
			"D m", "D aum", "D dim", "Eb", "Eb m", "Eb aum", "Eb dim", "E",
			"E m", "E aum", "E dim" };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		this.optionsMenu = menu;
//		MenuInflater inflater = this.getMenuInflater();
//		inflater.inflate(R.menu.sound_recording_example2, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.airport_menuRefresh:
//			MenuItem refreshItem = optionsMenu
//					.findItem(R.id.airport_menuRefresh);
//			refreshItem.setActionView(R.layout.actionbar);
//			// Complete with your code
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);

		myfft = new MyFFT();
		chordTV = (TextView) findViewById(R.id.Chord);
		chordTV.setTextSize(70);
		// Colocando o botao do microfone branco
		microphoneButton = this.getResources().getDrawable(
				drawable.ic_btn_speak_now);
		ColorFilter filter = new LightingColorFilter(Color.WHITE, Color.WHITE);
		microphoneButton.setColorFilter(filter);

		btnSetSoundGetChord = (ImageButton) findViewById(R.id.btnSetSoundGetChord);
		btnSetSoundGetChord.setImageDrawable(microphoneButton);
		btnSetSoundGetChord.setOnClickListener(btnClick);

		 barraCores = new ProgressBar[12];

		 pbC = (ProgressBar) findViewById(R.id.vertical_progressbarC);
		 pbC.setProgress(0);
		 barraCores[0] = pbC;
		 pbCs = (ProgressBar) findViewById(R.id.vertical_progressbarCs);
		 pbCs.setProgress(0);
		 barraCores[1] = pbCs;
		 pbD = (ProgressBar) findViewById(R.id.vertical_progressbarD);
		 pbD.setProgress(0);
		 barraCores[2] = pbD;
		 pbEb = (ProgressBar) findViewById(R.id.vertical_progressbarEb);
		 pbEb.setProgress(0);
		 barraCores[3] = pbEb;
		 pbE = (ProgressBar) findViewById(R.id.vertical_progressbarE);
		 pbE.setProgress(0);
		 barraCores[4] = pbE;
		 pbF = (ProgressBar) findViewById(R.id.vertical_progressbarF);
		 pbF.setProgress(0);
		 barraCores[5] = pbF;
		 pbFs = (ProgressBar) findViewById(R.id.vertical_progressbarFs);
		 pbFs.setProgress(0);
		 barraCores[6] = pbFs;
		 pbG = (ProgressBar) findViewById(R.id.vertical_progressbarG);
		 pbG.setProgress(0);
		 barraCores[7] = pbG;
		 pbGs = (ProgressBar) findViewById(R.id.vertical_progressbarGs);
		 pbGs.setProgress(0);
		 barraCores[8] = pbGs;
		 pbA = (ProgressBar) findViewById(R.id.vertical_progressbarA);
		 pbA.setProgress(0);
		 barraCores[9] = pbA;
		 pbBb = (ProgressBar) findViewById(R.id.vertical_progressbarBb);
		 pbBb.setProgress(0);
		 barraCores[10] = pbBb;
		 pbB = (ProgressBar) findViewById(R.id.vertical_progressbarB);
		 pbB.setProgress(0);
		 barraCores[11] = pbB;

		pbEnergy = (ProgressBar) findViewById(R.id.vertical_progressbarEnergy);

	}

	public void onClickHelp(View view) {

		System.out.println("Help Clicado!!!!");
		// MenuItem refreshItem = optionsMenu
		// .findItem(R.id.help);
		// refreshItem.setActionView(R.layout.actionbar);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (asyncTaskChords != null) {
			started = false;
			asyncTaskChords.cancel(true);
			asyncTaskChords = null;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.btnSetSoundGetChord: {
				if (started) {
					stopRecorders();

				} else {
					playRecorders();
				}
				break;
			}
			}
		}
	};

	public void stopRecorders() {
		started = false;
		asyncTaskChords.cancel(true);
		btnSetSoundGetChord.setImageDrawable(microphoneButton);
	}

	public void playRecorders() {
		System.out.println("Botao para comecar apertado");
		started = true;
		asyncTaskChords = null;
		asyncTaskChords = new RecordingAndSetChord();
		asyncTaskChords.execute();
		btnSetSoundGetChord.setImageResource(drawable.ic_media_pause);
	}

	private class RecordingAndSetChord extends AsyncTask<Void, byte[], Void> {

		@Override
		protected Void doInBackground(Void... params) {

			System.out.println("Do in background!!!");

			while (started) {
				// Inicializando vari�veis para a grava��o
				bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				// Instanciando array de bytes
				byte data[] = new byte[bufferSize];
				// Inicializando AudioRecord
				recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
						RECORDER_SAMPLERATE, RECORDER_CHANNELS,
						RECORDER_AUDIO_ENCODING, bufferSize);

				// Inicializando the ByteArrayOutPutStream
				ByteArrayOutputStream baos = null;
				baos = new ByteArrayOutputStream();
				// Verificando Status do objeto recorder
				int i = recorder.getState();
				if (i == 1) {
					recorder.startRecording();
				}

				long tempoInicio = System.currentTimeMillis();
				long tempoDecorrido = 0;
				while (tempoDecorrido <= 800) {
					int result = recorder.read(data, 0, data.length);
					if (result == AudioRecord.ERROR_INVALID_OPERATION) {
						throw new RuntimeException();
					} else if (result == AudioRecord.ERROR_BAD_VALUE) {
						throw new RuntimeException();
					} else {

						baos.write(data, 0, result);
					}
					tempoDecorrido = System.currentTimeMillis() - tempoInicio;
				}

				recorder.stop();
				recorder.release();
				recorder = null;

				publishProgress(baos.toByteArray());

				try {

					baos.close();
					baos = null;

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(byte[]... values) {

			myfft.setByteArraySong(values[0]);
			float[] S1 = myfft.getS1();
			
			float energy = myfft.getEnergy();
			pbEnergy.setProgress((int) ((100*(energy/minEnergy))));
			
			//0.4 = 80Db
			if (energy > minEnergy) {
				// Reordenando as notas nas barras de energia
				float[] valores = new float[12];
				for (int l = 0; l < S1.length; l++) {
					valores[l] = S1[(l + 7) % S1.length];
				}

				System.out.println("---------------------");
				for (int i = 0; i < 12; i++) {
					int ampli = (int) ((int) 100 * valores[i]);
					barraCores[i].setProgress(ampli);
				}

				// Setando valores na tela
				int numAcorde = myfft.getAcorde();
				chordTV.setText(nomeAcordes[numAcorde]);
				textGradient = new LinearGradient(0, 0, 0, 250,
						new int[] { coresNotas[(int) (numAcorde / 4)],
								Color.rgb(153, 47, 47) }, new float[] { 0, 1 },
						TileMode.CLAMP);
				chordTV.getPaint().setShader(textGradient);
			}
 			

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}

}