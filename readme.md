# Summary:
I made this library with single purpose, to auto format number while user type arbitrary number.
For example:
150000 -> 15,000.
276000800.50 -> 276,000,800.50

While this is easy to achieve using simple format, I find it challenging when you involve user interaction (on EditText), especially after inserting or deleting a text. With this library user can type money on EditText without worrying it will not incorrectly formatted.

# Demo:
[![AutoFormat in Action](http://img.youtube.com/vi/8Ef79UqwHfk/0.jpg)](http://www.youtube.com/watch?v=8Ef79UqwHfk)

# Usage:
add this on your app level gradle

```compile 'com.github.Maikerupitazu:AutoFormatEditText:0.2.0'```

# Example
See sample app to see it in action. It's very simple, just apply AutoFormatEditText like an EditText on your layout
```xml
<com.maikerupitazu.autoformatedittext.AutoFormatEditText
android:layout_width="match_parent"
android:layout_height="wrap_content"
app:isDecimal="true"
android:maxLength="10"/>
```
By default this doesn't support Decimal input, to enable decimal input use ```app:isDecimal="true"``` on its attribute. Value after Decimal will not be auto formatted. You can also use all TextView's attribute like maxLength to limit user input.

# Feature:
- Thousands separated number auto-format
- Value after dots will not be formatted i.e  732,000.502816
- Support with or without decimal formatting
- Various input restriction (Soft input keyboard and max length)

# You may find this library useful for:
- Calculator app
- Banking / Finance app
- Any app that involve auto formatting

# License
```
Copyright 2016 Michael Peters

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
