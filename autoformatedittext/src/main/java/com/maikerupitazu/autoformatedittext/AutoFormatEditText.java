package com.maikerupitazu.autoformatedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Locale;
import java.util.StringTokenizer;

import static com.maikerupitazu.autoformatedittext.AutoFormatUtil.getDecimalSeparator;
import static com.maikerupitazu.autoformatedittext.AutoFormatUtil.getDecimalSeparatorString;
import static com.maikerupitazu.autoformatedittext.AutoFormatUtil.getGroupingSeparator;
import static com.maikerupitazu.autoformatedittext.AutoFormatUtil.getGroupingSeparatorString;


/**
 * Created by aldo on 21/08/16.
 */
public class AutoFormatEditText extends EditText {

    private static final int MAX_LENGTH = 19;

    private int prevCommaAmount;

    private boolean isFormatting;

    private boolean isDecimal;

    private Locale locale = Locale.GERMAN;

    public AutoFormatEditText(Context context) {
        super(context);
        initialize();
    }

    public AutoFormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public AutoFormatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private void initialize() {
        initialize(null);
    }

    private void initialize(AttributeSet attrs) {
        setInputFilter(attrs);
        obtainAttributes(attrs);
    }

    private void setInputFilter(AttributeSet attrs) {
        int maxLength = MAX_LENGTH;
        if (attrs != null){
            int[] maxLengthAttrs = { android.R.attr.maxLength };
            TypedArray typedArrays = getContext()
                    .obtainStyledAttributes(attrs, maxLengthAttrs);
            try{
                maxLength = typedArrays.getInteger(0, MAX_LENGTH);
            }finally {
                typedArrays.recycle();
            }
        }

        // limit maximum length for input number
        InputFilter[] inputFilterArray = new InputFilter[1];
        inputFilterArray[0] = new InputFilter.LengthFilter(maxLength);
        setFilters(inputFilterArray);
    }

    private void obtainAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArrays = getContext()
                    .obtainStyledAttributes(attrs, com.maikerupitazu.autoformatedittext.R.styleable.AutoFormatEditText);

            try {
                isDecimal = typedArrays.getBoolean(com.maikerupitazu.autoformatedittext.R.styleable.AutoFormatEditText_isDecimal, false);
            } finally {
                typedArrays.recycle();
            }
        }
    }

    public void updateSoftInputKeyboard(boolean isDecimal) {
        this.isDecimal = isDecimal;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onTextChanged(CharSequence s, int start, int lengthBefore, int lengthAfter) {
        if (isFormatting) {
            return;
        }

        if (s.length() > 0) {
            formatInput(s, start, lengthAfter);
        }

        isFormatting = false;
    }

    private void formatInput(CharSequence s, int start, int count) {
        isFormatting = true;

        StringBuilder sbResult = new StringBuilder();
        String result;
        int newStart = start;

        try {
            // Extract value without its comma
            String digitAndDotText = s.toString().replace(getGroupingSeparatorString(locale), "");
            int commaAmount = 0;

            // if user press . turn it into 0.
            if (s.toString().startsWith(getDecimalSeparatorString(locale)) && s.length() == 1) {
                setText("0" + getDecimalSeparator(locale));
                setSelection(getText().toString().length());
                return;
            }

            // if user press . when number already exist turns it into comma
            if (s.toString().startsWith(getDecimalSeparatorString(locale)) && s.length() > 1) {
                StringTokenizer st = new StringTokenizer(s.toString());
                String afterDot = st.nextToken(getDecimalSeparatorString(locale));
                setText("0" + getDecimalSeparator(locale) + AutoFormatUtil.extractDigits(afterDot));
                setSelection(2);
                return;
            }

            if (digitAndDotText.contains(getDecimalSeparatorString(locale))) {
                // escape sequence for .
                String[] wholeText = digitAndDotText.split(getDecimalSeparatorString(locale));

                if (wholeText.length == 0) {
                    return;
                }

                // in 150,000.45 non decimal is 150,000 and decimal is 45
                String nonDecimal = wholeText[0];
                if (nonDecimal.length() == 0) {
                    return;
                }

                // only format the non-decimal value
                result = AutoFormatUtil.formatToStringWithoutDecimal(nonDecimal, locale);

                sbResult
                        .append(result)
                        .append(getDecimalSeparatorString(locale));

                if (wholeText.length > 1) {
                    sbResult.append(wholeText[1]);
                }

            } else {
                result = AutoFormatUtil.formatWithDecimal(digitAndDotText, locale);
                sbResult.append(result);
            }

            // count == 0 indicates users is deleting a text
            // count == 1 indicates users is entering a text
            newStart += ((count == 0) ? 0 : 1);

            // calculate comma amount in edit text
            commaAmount += AutoFormatUtil.getCharOccurance(result, getGroupingSeparator(locale));

            // flag to mark whether new comma is added / removed
            if (commaAmount >= 1 && prevCommaAmount != commaAmount) {
                newStart += ((count == 0) ? -1 : 1);
                prevCommaAmount = commaAmount;
            }

            // case when deleting without comma
            if (commaAmount == 0 && count == 0 && prevCommaAmount != commaAmount) {
                newStart -= 1;
                prevCommaAmount = commaAmount;
            }

            // case when deleting without dots
            if (count == 0 && !sbResult.toString()
                    .contains(getDecimalSeparatorString(locale)) && prevCommaAmount != commaAmount) {
                newStart = start;
                prevCommaAmount = commaAmount;
            }

            setText(sbResult.toString());

            // ensure newStart is within result length
            if (newStart > sbResult.toString().length()) {
                newStart = sbResult.toString().length();
            } else if (newStart < 0) {
                newStart = 0;
            }

            setSelection(newStart);

        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}