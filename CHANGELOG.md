?.?.?
-----

* fixed licenses, accidentally had it shifted to GPL; reverted to LGPL
* the `DEFAULT_MARSHALLER` in `org.jcamp.parser.NoteMarshallerFactory` now gets
  initialized properly
* `org.jcamp.parser.NMRJCAMPWriter` now uses the height of the peak when writing
  the peak table instead of the second value of the position as per specs


0.9.3
-----

* changed Maven setup to use github URLs for group and scm
* clean up of `pom.xml`
* added README


0.9.2
-----

New:

* added `JCAMPReader.createSpectrum(Reader)` method
* code cleanup

  * introduced serialVersionUIDs
  * replaced StringBuffer with StringBuilder

* can be built using Apache Maven now as well
* JCAMPReader now has a STRICT and RELAXED mode. For documentation, 
  see STRICT constant in JCAMPReader. Further usages of this flag 
  should add documentation there.

Fixes:

* numeric values in header can be format `#.#` or `#,#` now
* Labels in a comment (like `$$ ##TITLE=`) confused the parser
* The parser reader now shows same behaviour with respect to units 
  first and second time (this influenced if hertz-ppm-conversion was 
  done in nmr, so rather big effects)
* Reading of floats in XYDATA now works (thanks to Ralf Tralow for that)


0.9.1
-----

New:

* Handling of multi block files (see doc)

Fixes:

* Some bugfixes


0.9.0
-----

No changes, initial release  
