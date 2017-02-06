This Android application is an example of reactive programming using RxAndroid, RxJava, and Retrofit 2; this program implements the "observer pattern". The purpose of this project is to further learn about the observer pattern and useful reactive programming concepts.

What is reactive programming?
Reactive programming makes use of asynchronous data streams, which can be observed for changes and reacted upon accordingly.

Generally, a data stream is a sequence of events in time; it can emit a value, an error, or a 'completed' signal. These emitted events are captured asynchronously, where corresponding methods are called for handling each of these three possible outcomes (emitted value, error, or completed signal). Subscribers are what listen to the stream, and the defined functions are observers. The stream is the "Observable" being observed. This is what is known as the observer pattern.

