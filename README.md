LicenseRecognition
==================

LicenseRecognition is an open source project that recognizes licenses of Car using an Android app to take pictures of license. The project analyzes images, identifies license plates, and send back result to users. The output is the text representation of any license plate characters found in the image.

## Android app screenshots
<img src="https://raw.github.com/yang2012/LicenseRecognition/master/images/1.png" width="30%" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://raw.github.com/yang2012/LicenseRecognition/master/images/2.png" width="30%" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://raw.github.com/yang2012/LicenseRecognition/master/images/3.png" width="30%" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://raw.github.com/yang2012/LicenseRecognition/master/images/4.png" width="30%" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://raw.github.com/yang2012/LicenseRecognition/master/images/5.png" width="30%" />


## Environment
___________
This project require following ilbraries and tools:

* Ubuntu 12.04
* opencv 2.4.7.1
* tesseract-ocr 3.02.02
* openalpr
* teptonica-1.70
* java 1.7.0_51
* eclipse Kepler Service Release 2

## Installation
___________
### Install opencv
To install OpenCV 2.4.2 or 2.4.3 on the Ubuntu 12.04 operating system, first install a developer environment to build OpenCV.

    sudo apt-get -y install build-essential cmake pkg-config
Install Image I/O libraries

    sudo apt-get -y install libjpeg62-dev 
    sudo apt-get -y install libtiff4-dev libjasper-dev
Install the GTK dev library

    sudo apt-get -y install  libgtk2.0-dev
Install Video I/O libraries

    sudo apt-get -y install libavcodec-dev libavformat-dev libswscale-dev libv4l-dev
Optional - install support for Firewire video cameras

	sudo apt-get -y install libdc1394-22-dev
Optional - install video streaming libraries

	sudo apt-get -y install libxine-dev libgstreamer0.10-dev libgstreamer-plugins-base0.10-dev 
Optional - install the Python development environment and the Python Numerical library

    sudo apt-get -y install python-dev python-numpy
 
Optional - install the parallel code processing library (the Intel tbb library)

    sudo apt-get -y install libtbb-dev
Optional - install the Qt dev library

    sudo apt-get -y install libqt4-dev
Now download OpenCV 2.4 to wherever you want to compile the source.

    mkdir xxx
    cd xxx 
    wget https://github.com/Itseez/opencv/archive/2.4.7.tar.gz
or

	wget http://sourceforge.net/projects/opencvlibrary/files/opencv-unix/2.4.7/opencv-2.4.7.tar.gz

then
	
	tar -zxvf OpenCV-2.4.7.gz
	
Create and build directory and onfigure OpenCV with cmake. Don't forget the .. part at the end of cmake cmd !!

    cd OpenCV-2.4.*
    mkdir build
    cd build
    cmake -D CMAKE_BUILD_TYPE=RELEASE -D CMAKE_INSTALL_PREFIX=/usr/local
    -D WITH_TBB=ON -D BUILD_NEW_PYTHON_SUPPORT=ON -D WITH_V4L=ON 
    -D INSTALL_C_EXAMPLES=ON -D INSTALL_PYTHON_EXAMPLES=ON 
    -D BUILD_EXAMPLES=ON -D WITH_QT=ON -D WITH_OPENGL=ON ..
Now compile it

    make
And finally install OpenCV

    sudo make install

### Install tesseract-ocr
First install the required libraries and tools for compiling.

	sudo apt-get install libpng-dev libjpeg-dev libtiff-dev zlib1g-dev
	sudo apt-get install gcc g++
	sudo apt-get install autoconf automake libtools checkinstall

Install Leptonica from [source](http://www.leptonica.org/source/leptonica-1.70.tar.gz). The latest version as of writing is 1.70.

	wget http://www.leptonica.org/source/leptonica-1.70.tar.gz
	tar -zxvf leptonica-1.70.tar.gz
	cd leptonica-1.70
	./configure
	make
	sudo checkinstall
	sudo ldconfig

Then install Tesseract OCR from source.

	wget https://tesseract-ocr.googlecode.com/files/tesseract-ocr-3.02.02.tar.gz
	tar -zxvf tesseract-ocr-3.02.02.tar.gz
	cd tesseract-ocr
	./autogen.sh
	./configure
	make (this may take a while)
	sudo make install
	sudo ldconfig

Finally, install the languages you want. Simply place the trained data under `/usr/local/share/tessdata`. You can do this through wget or FTP upload.

### Compile openalpr

At first, you should clone openalpr from github:

	cd <your_work_space>
	git clone https://github.com/openalpr/openalpr.git
	cd openalpr-master

Then update the `src/CMakeLists.txt` file in the OpenALPR project. Update the following lines to match the directories of your libraries on your system:

- SET(OpenCV_DIR "<path_to_opencv>")
- SET(Tesseract_DIR "<path_to_esseract-ocr>")

Finally, in the src directory, execute the following commands:

	cmake ./
	make

### Compile LicenseRecognition

Finally, you just need to clone `LicenseRecognition` project

	cd <your_work_space>
	git clone https://github.com/yang2012/LicenseRecognition.git

And open it using Eclipse. Then modify `Constants.java` file to correctly set `kUploadImageContainerDirectory` and `kOpenalprDirectory`. 

At last, just run it. That's it.


## TODO
___________
1. support Chinese characters
2. Improve recognition rate

## License
___________

This project is under the MIT License (MIT) [https://github.com/yang2012/LicenseRecognition/blob/master/LICENSE](https://github.com/yang2012/LicenseRecognition/blob/master/LICENSE)