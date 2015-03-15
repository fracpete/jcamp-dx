# Code Examples #
The main purpose of the library is to read and to write JCAMP-DX files. The
classes in `org.jcamp.spectrum` are the objects read/written. So you will
always use these.

## Reading a file ##

Let's look at a first example: You have a jcamp file and want to read the peaks
from it. So firstly you read the file – the class to use is
`JCAMPReader` in `org.jcamp.parser` like this:

```
Spectrum jcampSpectrum = JCAMPReader.getInstance().createSpectrum(jcamp);
NMRSpectrum nmrspectrum = (NMRSpectrum) jcampSpectrum;
```

`JCAMPReader` is a singleton, it returns one of the subclasses of
Spectrum. Here we know the spectrum is an NMR one, so we can directly cast it.
Have a look at the API doc to see the different types of spectra and their
inheritance relationships. Then we look if the spectrum has got peaks:

```
if (nmrspectrum.hasPeakTable()) {
  Peak[] peaks = nmrspectrum.getPeakTable();
  for (int i = 0; i < peaks.length; i++) {
    ... = (float) peaks[i].getPosition()[0];
    ... = (float) peaks[i].getHeight();
  }
}
```

If it has peaks (remember JCAMP-DX can also contain continuous data), it's an
array of Peak objects. Each peak has a got a multi-dimensional position (for 2D
etc. spectra) and a height. In case of our nmr spectrum, these would be shift
and intensity, so we read them to any variable (... is your code).

A JCAMP-DX file can contain several blocks (a block basically is a spectrum). In
such a case, the above code will only return the first child block. In order to
access all blocks, you can use
`JCAMPReader.getInstance().getRootblock()!=null` to check if it is a
multi block file. If so, use something like this to read them:

```
Enumeration blocks=JCAMPReader.getInstance().getRootblock().getBlocks();
while(blocks.hasMoreElements()){
  JCAMPBlock b = (JCAMPBlock) blocks.nextElement();
  if(b.getID()!=JCAMPReader.getInstance().getIdoffirstspectrum()){
    Spectrum spectrum2=JCAMPReader.getInstance().createSpectrum(JCAMPReader.getInstance().rootblock, b.getID());
    if(spectrum2.isFullSpectrum())
      cmlSpectrum.addSpectrumData(mapContData((Spectrum1D)spectrum2));
    else
      cmlSpectrum.addPeakList(mapPeaks((Spectrum1D)spectrum2));
    break;
  }
}
```

(`idoffirstspectrum` contains the id of the spectrum we already get ``directly'').

## Writing Files ##

If you want to do it the other way round (i. e. write a file), you first need to
build a Spectrum (look at the javadoc to see what parameters mean):

```
double[][] xy = new Double[2][peaknumber];
//this array needs to be filled with x/y values in 0/1 position respectivly
IOrderedDataArray1D x = new OrderedArrayData(xy[0],CommonUnit.mz);
IDataArray1D y = new ArrayData(xy[1], CommonUnit.intensity);
Spectrum1D jdxspectrum = new NMRSpectrum(x, y, nucleus, freq, reference, true);
```

Then we set peaks on it (indeed values are given twice to the writer – a bit
confusing, admittedly):

```
Peak1D[] jcampPeaks = new Peak1D[5];
Peak1D jcampPeak = new Peak1D(x,y);
jcampPeak.setHeight(y);
jcampPeaks[0] = jcampPeak;
... for all peaks
jdxspectrum.setPeakTable(jcampPeaks);
```

Finally we write it:

```
JCAMPWriter jcamp = JCAMPWriter.getInstance();
String jcampString = jcamp.toJCAMP(jdxspectrum);
```

For specific questions, please consult the Javadoc.