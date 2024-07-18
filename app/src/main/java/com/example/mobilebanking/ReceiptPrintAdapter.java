package com.example.mobilebanking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ReceiptPrintAdapter extends PrintDocumentAdapter {

    private final Context context;
    private final View contentView;

    public ReceiptPrintAdapter(Context context, View contentView) {
        this.context = context;
        this.contentView = contentView;
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        int marginMillimeters = (int) (0.5 * 25.4);
        PrintAttributes.Margins margins = new PrintAttributes.Margins(marginMillimeters, marginMillimeters, marginMillimeters, marginMillimeters);

        PrintAttributes printAttributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 100, 100))
                .setMinMargins(margins)
                .build();
        PrintedPdfDocument pdfDocument = new PrintedPdfDocument(context, printAttributes);

        // Start the PDF creation
        PdfDocument.Page page = pdfDocument.startPage(0);

        // Get the Canvas to draw on the PDF
        Canvas canvas = page.getCanvas();

        // Measure and layout the content view on the canvas
        contentView.measure(
                View.MeasureSpec.makeMeasureSpec(canvas.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(canvas.getHeight(), View.MeasureSpec.EXACTLY)
        );
        contentView.layout(-canvas.getWidth() / 8, 0, canvas.getWidth(), canvas.getHeight());
        int contentViewWidth = contentView.getMeasuredWidth();
        int contentViewHeight = contentView.getMeasuredHeight();
        Log.d("PDF_DEBUG", "Content View Width: " + contentViewWidth);
        Log.d("PDF_DEBUG", "Content View Height: " + contentViewHeight);

        // Draw the content view onto the canvas
        contentView.draw(canvas);

        // Finish the PDF creation
        pdfDocument.finishPage(page);

        try {
            OutputStream outputStream = new FileOutputStream(destination.getFileDescriptor());
            pdfDocument.writeTo(outputStream);
            pdfDocument.close();

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        } catch (IOException e) {
            e.printStackTrace();
            callback.onWriteFailed(null);
        }
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        PrintDocumentInfo info = new PrintDocumentInfo.Builder("receipt.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .build();

        callback.onLayoutFinished(info, true);
    }
}
