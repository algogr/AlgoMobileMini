package gr.algo.algomobilemini;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import hr.istratech.bixolon.driver.charset.ByteCharset;
import hr.istratech.bixolon.driver.command.general.Alignment;
import hr.istratech.bixolon.driver.command.print.*;
import hr.istratech.bixolon.driver.command.qr.QrCodeErrorCorrectionLevel;
import hr.istratech.bixolon.driver.command.qr.QrCodeModel;
import hr.istratech.bixolon.driver.command.qr.QrCodeSize;
import hr.istratech.bixolon.driver.general.Printer;
import hr.istratech.bixolon.driver.general.QrPrinterBuilder;
import hr.istratech.bixolon.driver.general.TextPrinterBuilder;

import java.nio.charset.Charset;


/**
 * @author ksaric
 */

public class BluetoothBixolonActivity extends Activity {

    private static final String TAG = "BluetoothBixolonActivity";
    public static final String NEW_LINE = "\r\n";

    private Button connectButton;

    private CheckBox charsetsCheckBox;
    private Button charsetButton;

    private TextView textEditText;
    private Button textPrintButton;
    private Button textPremutationsPrintButton;

    private TextView qrEditText;
    private Button qrPrintButton;
    private Button qrPermutationsPrintButton;

    private Button closeButton;

    private BluetoothSPP bluetoothSPP;
    private UserFeedback userFeedback;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        this.setContentView( R.layout.activiy_bixolon );

        this.connectButton = (Button) this.findViewById( R.id.connect_button );

        this.charsetsCheckBox = (CheckBox) this.findViewById( R.id.charsets_checkbox );
        this.charsetButton = (Button) this.findViewById( R.id.charset_button );

        this.textEditText = (TextView) this.findViewById( R.id.text_edit_text );

        this.textPrintButton = (Button) this.findViewById( R.id.text_print_button );
        this.textPremutationsPrintButton = (Button) this.findViewById( R.id.text_permutations_print_button );

        this.qrEditText = (TextView) this.findViewById( R.id.qr_text );

        this.qrPrintButton = (Button) this.findViewById( R.id.qr_print_button );
        this.qrPermutationsPrintButton = (Button) this.findViewById( R.id.qr_premutations_print_button );

        this.closeButton = (Button) this.findViewById( R.id.close_button );


        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        if ( !bluetoothSPP.isBluetoothEnabled() ) {
            // Do somthing if bluetooth is disabled
        } else {
            // Do something if bluetooth is already enabled
        }
    }

    private void init() {

        bluetoothSPP = new BluetoothSPP( this );
        userFeedback = new UserFeedback( this );

        this.connectButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                connect();
            }
        } );

        this.charsetButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                printCharsets();
            }
        } );

        this.textPrintButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                printText();
            }
        } );
        this.textPremutationsPrintButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                printTextPermutations();
            }
        } );

        this.qrPrintButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                printQr();
            }
        } );
        this.qrPermutationsPrintButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                printQrPermutations();
            }
        } );

        this.closeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                bluetoothSPP.disconnect();
            }
        } );

        // first pass
        charsetsCheckBox.setChecked( false );

        charsetsCheckBox.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                charsetsCheckBox.setChecked( charsetsCheckBox.isChecked() );
            }
        } );
    }

    private String printCodePageChars( final CodePage codePage ) {
        final StringBuilder stringBuilder = new StringBuilder();
        final ByteCharset charset = codePage.getCharset();

        for ( final char c : charset.getLookupTable() ) {
            stringBuilder.append( c );
        }

        stringBuilder.append( NEW_LINE );
        stringBuilder.append( NEW_LINE );

        return stringBuilder.toString();
    }


    private String printCodePageCharsGeneric( final CodePage codePage ) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Charset charset = codePage.getCharset();

        for ( int i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++ ) {
            final String s = Character.toString( (char) i );

            final byte[] encoded = s.getBytes( charset );
            final String decoded = new String( encoded, charset );

            if ( s.equals( decoded ) ) {
                stringBuilder.append( s );
            }
        }

        stringBuilder.append( NEW_LINE );
        stringBuilder.append( NEW_LINE );

        return stringBuilder.toString();
    }

    private void connect() {

        if ( bluetoothSPP.isBluetoothEnabled() && bluetoothSPP.isBluetoothAvailable() ) {

            if ( !bluetoothSPP.isServiceAvailable() ) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService( BluetoothState.DEVICE_OTHER );
            }

            if ( bluetoothSPP.isServiceAvailable() ) {

                Log.i( "JIM", "+++ Connecting device +++" );

                bluetoothSPP.setAutoConnectionListener( new BluetoothSPP.AutoConnectionListener() {


                    @Override
                    public void onAutoConnectionStarted() {
                        Log.v( "JIM", "+++ Bluetooth device connected +++" );
                    }

                    @Override
                    public void onNewConnection( final String name, final String address ) {
                        Log.v( "JIM", "+++ New connection on '" + name + " :: " + address + "' +++" );
                    }
                } );

                bluetoothSPP.autoConnect( "SPP-" );

                bluetoothSPP.setOnDataReceivedListener( new BluetoothSPP.OnDataReceivedListener() {
                    public void onDataReceived( byte[] data, String message ) {
                        // Do something when data incoming
                        Log.d("JIM","DATA RECEIVED");
                        userFeedback.alert( message );
                    }
                } );

            }
        } else {
            userFeedback.error( "Connect and enable bluetooth!" );
            return;
        }
    }

    public void printCharsets() {
        final boolean isChecked = charsetsCheckBox.isChecked();

        if ( isChecked ) {
            for ( final CodePage codePage : CodePage.values() ) {
                final Printer printer = TextPrinterBuilder
                        .aPrinterBuilder()
                        .withCodePage( codePage )
                        .withGeneralControlSequence( Alignment.LEFT )
                        .withTextControlSequence( CharacterSize.NORMAL )
                        .withTextControlSequence( DeviceFont.DEVICE_FONT_A )
                        .buildPrinter( printCodePageChars( codePage ) );

                bluetoothSPP.send( printer.getCommand(), false );
            }

        } else {
            final CodePage codePage = CodePage.CP_437_USA;

            final Printer printer = TextPrinterBuilder
                    .aPrinterBuilder()
                    .withCodePage( codePage )
                    .withGeneralControlSequence( Alignment.LEFT )
                    .withTextControlSequence( CharacterSize.NORMAL )
                    .withTextControlSequence( DeviceFont.DEVICE_FONT_A )
                    .buildPrinter( printCodePageChars( codePage ) );

            bluetoothSPP.send( printer.getCommand(), false );
        }

    }

    public void printTextPermutations() {
        final CharSequence textTextContent = this.textEditText.getText();

        for ( final Alignment alignment : Alignment.values() ) {
            for ( final Emphasize emphasize : Emphasize.values() ) {
                for ( final Underline underline : Underline.values() ) {
                    for ( final Reverse reverse : Reverse.values() ) {
                        for ( final CharacterSize characterSize : CharacterSize.values() ) {
                            for ( final DeviceFont deviceFont : DeviceFont.values() ) {
                                final Printer printer = TextPrinterBuilder
                                        .aPrinterBuilder()
                                        .withCodePage( CodePage.CP_437_USA )
                                        .withGeneralControlSequence( alignment )
                                        .withTextControlSequence( characterSize )
                                        .withTextControlSequence( deviceFont )
                                        .withTextControlSequence( emphasize )
                                        .withTextControlSequence( underline )
                                        .withTextControlSequence( reverse )
                                        .buildPrinter( textTextContent.toString() );

                                bluetoothSPP.send( printer.getCommand(), false );
                            }
                        }
                    }
                }
            }
        }
    }

    public void printText() {
        final CharSequence textTextContent = this.textEditText.getText();


        final Printer printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage( CodePage.CP_437_USA )
                .withGeneralControlSequence( Alignment.LEFT )
                .withTextControlSequence( CharacterSize.NORMAL )
                .withTextControlSequence( DeviceFont.DEVICE_FONT_A )
                .buildPrinter( textTextContent.toString() );

        bluetoothSPP.send( printer.getCommand(), false );
    }

    public void printQrPermutations() {
        final CharSequence qrTextContent = this.qrEditText.getText();

        for ( final Alignment alignment : Alignment.values() ) {
            for ( final QrCodeModel qrCodeModel : QrCodeModel.values() ) {
                for ( final QrCodeSize qrCodeSize : QrCodeSize.values() ) {
                    for ( final QrCodeErrorCorrectionLevel errorCorrectionLevel : QrCodeErrorCorrectionLevel.values() ) {
                        final Printer printer = QrPrinterBuilder
                                .aPrinterBuilder()
                                .withGeneralControlSequence( alignment )
                                .withQrControlSequence( qrCodeModel )
                                .withQrControlSequence( qrCodeSize )
                                .withQrControlSequence( errorCorrectionLevel )
                                .buildPrinter( qrTextContent.toString() );

                        bluetoothSPP.send( printer.getCommand(), false );
                    }
                }
            }
        }
    }

    public void printQr() {

        final CharSequence qrTextContent = this.qrEditText.getText();

        final Printer printer = QrPrinterBuilder
                .aPrinterBuilder()
                .withGeneralControlSequence( Alignment.CENTER )
                .withQrControlSequence( QrCodeModel.MODEL2 )
                .withQrControlSequence( QrCodeSize.SIZE7 )
                .withQrControlSequence( QrCodeErrorCorrectionLevel.L )
                .buildPrinter( qrTextContent.toString() );

        bluetoothSPP.send( printer.getCommand(), false );

    }

    public static class UserFeedback {

        private final Context context;

        public UserFeedback( final Context context ) {
            this.context = context;
        }

        private Activity getActivity() {
            return (Activity) context;
        }

        public void alert( final String message ) {
            if ( !getActivity().isFinishing() )
                showPopup( context, message, "Alert" );
        }

        public void success( final String message ) {
            if ( !getActivity().isFinishing() )
                showPopup( context, message, "Success" );
        }

        public void error( final String message ) {
            if ( !getActivity().isFinishing() )
                showPopup( context, message, "Error" );
        }

        public void longToast( final String message ) {
            if ( !getActivity().isFinishing() )
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
        }

        public void shortToast( final String message ) {
            if ( !getActivity().isFinishing() )
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
        }

        public void showPopup( final Context context, final String alertText, final String title ) {
            AlertDialog alertDialog = new AlertDialog.Builder( context )
                    .setTitle( title )
                    .setMessage( alertText )
                    .create();

            // Setting OK Button
            alertDialog.setButton( DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                public void onClick( DialogInterface dialog, int which ) {

                }
            } );

            // Showing Alert Message
            alertDialog.show();
        }

    }

}
