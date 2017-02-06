This Android application is an example of reactive programming using RxAndroid, RxJava, and Retrofit 2; this program implements the "observer pattern". The purpose of this project is to further learn about the observer pattern and useful reactive programming concepts.

What is reactive programming?
Reactive programming makes use of asynchronous data streams, which can be observed for changes and reacted upon accordingly.

Generally, a data stream is a sequence of events in time; it can emit a value, an error, or a 'completed' signal. These emitted events are captured asynchronously, where corresponding methods are called for handling each of these three possible outcomes (emitted value, error, or completed signal). Subscribers are what listen to the stream, and the defined functions are observers. The stream is the "Observable" being observed. This is what is known as the observer pattern.

By default, the image file will be downloaded to /data/data/{PACKAGE_NAME}/images/.
To access the file, you can use the following steps (Linux):

1) Create a backup of the app's data: 
adb backup -noapk com.your.packagename (i.e. com.example.khe11e.rxdownloadfile)

2) Extract the compressed data:
dd if={backup_name.ab} bs=1 skip=24 > my-compressed-backup

3) Decompress the compressed data:
printf "\x1f\x8b\x08\x00\x00\x00\x00\x00" \ | cat - my-compressed-backup | gunzip -c > my-decompressed-backup.tar

4) Untar the tar file:
tar xf my-decompressed-backup.tar

You can then view the downloaded image file (in this case, named "RxImage1").
