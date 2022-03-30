
<html><head>
<meta http-equiv="content-type" content="text/html; charset=windows-1252">
<!-- META NAME="ROBOTS" CONTENT="NOINDEX, NOFOLLOW" -->

<title>  Cvt2Mae Data Converter
</title>
</head>

<body vlink="#FFad0f" text="Black" link="blue" bgcolor="#FBFBFB" alink="#00FF00">

<center>
<h2> Cvt2Mae Data Converter</h2>
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/Install/install.html">
<img src="C2Micon.gif" alt="Install Cvt2Mae on your computer" align="middle"></a>
</center>
<pre>
</pre>

<font size="-1">
<font face="Helvetica,Arial,sans-serif">
<center>
Cvt2Mae Home |
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mDesc.html">Description</a> |
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mInstall.html">Download</a> |
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mFAQ.html"> FAQ</a> |
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mAppendix.html">Appendix </a> |
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mAffyExample.html">Example </a> |
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mRevision.html">Revisions</a>  |
<a href="#updateC2M">Update</a>  |
<a href="http://maexplorer.sourceforge.net/MaeRefMan/hmaeIndex.html" target="_top">MAExplorer home</a> |
<a href="mailto:mae@ncifcrf.gov?subject:hmae-help-desk">Help Desk</a><br>
</center>
</font>
</font>

<pre>
</pre>

<h3>Cvt2Mae Basics</h3>

In order to use the <a href="http://maexplorer.sourceforge.net/MaeRefMan/hmaeIndex.html">MAExplorer</a> data-mining tool
on your cDNA or oligo tab-delimited array data, you must convert your
data files into the data formats described in <a href="http://maexplorer.sourceforge.net/MaeRefMan/hmaeDocC.html"> Appendix C</a> and <a href="http://maexplorer.sourceforge.net/MaeRefMan/hmaeDocD.html"> Appendix D</a> of the <a href="http://maexplorer.sourceforge.net/MaeRefMan/hmaeHelp.html">MAExplorer reference
manual</a>. Although this maybe done by editing user's data files by
hand into the required formats, it is a non-trivial process. Therefore
we have developed a "wizard" conversion tool called Cvt2Mae to
automate these conversions.

<p>

Cvt2Mae is a Java program designed to make it easier for use by
researchers to use MAExplorer by helping them convert their data into
the MAExplorer format. Cvt2Mae handles commercial chips such as
Affymetrix, as well as other standard formats such as GenePix and
Scanalyze or one-of-a-kind custom academic chips
(&lt;User-defined&gt;).  In addition, you may specify the fields of
interest for the "Print file" or (GIPO or Gene-In-Plate-Order) file,
and the fields containing the quantified data.

</p><p>

The Cvt2Mae converts specific chip information you entered into what
we call an "Array Layout". This Array Layout file may be edited and
saved for use in future conversions and shared with collaborators.
Essentially, the Array Layout contains a set of "rules" for converting
the user's data.  After you have filled out the forms in Cvt2Mae, it
will generate the set of converted data files and directories to be
used directly with MAExplorer.

</p><p>

<a name="pdfDocs">
</a></p><h3><a name="pdfDocs">Cvt2Mae Data Conversion Steps and Tutorials</a></h3><a name="pdfDocs">

There is a detailed description on </a><a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mDesc.html"> using the
Cvt2Mae converter</a> that provides the level of detail you need to
use it effectively. In addition, a step by step example is provided
converting <a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mAffyExample.html"> Affymetrix</a> data.

<p>

There are several slide shows describing how to use the Cvt2Mae to
convert various data sets. They consist of a series of screen shots
from Cvt2Mae that go through each of the steps on how to set up the
parameters and convert your data. There are two for Affymetrix data, one
is a downloadable PDF and other an extensive online version.

</p><p>

</p><h3>Tutorials</h3> 

<ol>
  <li> <a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mAffyExample.html">Affymetrix Data with a full
description (HTML)</a>

  </li><li> Affymetrix Data 
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PDF/ConvertingAffyArrayDataToMAExplorer.pdf">
       (PDF)</a> or
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PPT/ConvertingAffyArrayDataToMAExplorer.ppt">
       (PPT)</a>

  </li><li> GenePix Data 
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PDF/ConvertingGenePixArrayDataToMAExplorer.pdf">
       (PDF)</a> or
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PPT/ConvertingGenePixArrayDataToMAExplorer.ppt">
       (PPT)</a>

  </li><li> Scanalyze Data
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PDF/ConvertingScanalyzeArrayDataToMAExplorer.pdf">       
       (PDF)</a> or 
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PPT/ConvertingScanalyzeArrayDataToMAExplorer.ppt">       
       (PPT)</a>

  </li><li> &lt;User-defined&gt; Data 
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PDF/ConvertingUserDefinedArrayDataToMAExplorer.pdf">
       (PDF)</a> or
       <a href="http://maexplorer.sourceforge.net/MaeRefMan/PPT/ConvertingUserDefinedArrayDataToMAExplorer.ppt">
       (PPT)</a>

  </li><li> <a href="http://maexplorer.sourceforge.net/MaeRefMan/PDF/ConvertingIncyteArrayDataToMAExplorer.pdf">
       Incyte Data PDF</a>

  </li></ol>
  <p>


</p><p>

</p><h3>Downloading latest Cvt2Mae Version</h3>

You may freely download and install the current release of the Cvt2Mae
stand-alone application. You are free to use or redistribute
Cvt2Mae. You may want to review the <a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mRevision.html">revision history.</a>

<p>

<a href="http://maexplorer.sourceforge.net/Cvt2Mae/Install/install.html">
<img src="Cvt2Mae%20Data%20Converter_files/download.gif"></a> Download and Install Cvt2Mae.

</p><p>

Instructions on <a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mInstall.html">downloading and installing Cvt2Mae.</a>

<a name="updateC2M"></a></p><p><a name="updateC2M">
</a></p><h3><a name="updateC2M">Update Cvt2Mae Program from maexplorer.sourceforge.net</a></h3><a name="updateC2M">

As of version 0.71.1 of Cvt2Mae, it is now possible to update the
Cvt2Mae program from the program itself - rather than having to
download the complete installer and then running the installer.  Press
the "Update Cvt2Mae" button at the lower left of the corner of Cvt2Mae
when it is running. It asks if you want to update Cvt2Mae.  Answer
yes. This will then (1) backup the current Cvt2Mae.jar file as
Cvt2Mae.jar.bkup in the directory where you had initially installed
Cvt2Mae; (2) it then copies the latest Cvt2Mae.jar file from the
maexplorer.sourceforge.net Web site and replaces your working
Cvt2Mae.jar file in your installation directory.  You must restart
Cvt2Mae for this to take effect. It will then use the new version of
the program. This is a much less time consuming alternative than doing
an entire download and reinstallation from the Web site.

<p>

</p><h3>Frequently Asked Questions (FAQ)</h3>

Here are some questions you might have about the Cvt2Mae data converter.

</a><p><a name="updateC2M">

</a><a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mFAQ.html">FAQ </a>

</p><h3>If Additional Help is Needed </h3>

Before emailing us for help, please read these Cvt2Mae Web pages to
ensure that you have set the parameters correctly and have the raw
data in the correct format. You might also read the <a href="http://maexplorer.sourceforge.net/MaeRefMan/hmaeDocC.html"> Appendix C of the MAExplorer
manual.</a> MAExplorer and Cvt2Mae also create <a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mInstall.html#logfile">log files</a> that might be of helpful
in troubleshooting.

<p>

If you then are still having problems email the <a href="mailto:mae@ncifcrf.gov?subject:hmae-help-desk"> help
desk</a>. Please include:

</p><ul>

<li> A detailed description of the problem including error messages
</li><li> Information about the computer system (memory, operating system etc.)
</li><li> What array chip you are using, describe in detail if it is not
one of the default Array Layouts.

</li></ul>

<p>



</p><hr>

<center>
<font size="2" face="Helvetica, Arial,sans-serif">
[<a href="http://maexplorer.sourceforge.net/MaeRefMan/hmaeIndex.html"> MAExplorer home</a> |
<a href="http://maexplorer.sourceforge.net/Cvt2Mae/docs/c2mIndex.html" target="_top">Cvt2Mae home</a> |
<a href="mailto:mae@ncifcrf.gov?subject:hmae-help-desk">Help desk</a> |
<a href="http://www.lecb.ncifcrf.gov/" target="doc">LECB</a>/NCI/FCRDC ]
</font>
</center>


</body></html>
