# JavaFX
Demos of multi-threading and JavaFX programs

## Structure
* src
	* JavaFXClientServer: A GUI(JavaFX) based C/S multi-thread dictionary.
	* JavaFXDemo: JavaFX demo programs.
	* JavaFXfxmlDemo: JavaFX demo programs, using fxml files to create layouts.
	* multi_thread_extends: Multi-thread implementation by extending Thread class.
	* multi_thread_implement: Multi-thread implementation by implementing Runnable class.
	* multi_thread_lambda: Multi-thread implementation with lambda grammar.
	* multi_thread_multi_turn: A non-GUI based C/S multi-thread demo program.
	* multi_turn: A non-GUI based C/S multiple turn communication demo program.
	* single_turn: A non-GUI based C/S single turn communication demo program.

* notes
	* client_server_communication: a graph of the general communication progress between client and server, demonstrated by `add word` functionality in a C/S multi-threaded dictionary.

## Notes
* [JDK 13 download](http://www.oracle.com)
* [Java environment variable configuration](https://blog.csdn.net/vvv_110/article/details/72897142)
* [Download JavaFX](https://gluonhq.com/products/javafx/)
* [Use JavaFX in Intellij Idea](https://openjfx.io/openjfx-docs/#gradle)
* [Create jar in Intellij Idea](https://www.javatt.com/p/79407)
*  Enable parallel run for the same class: search `Allow running in parallel` [here](https://blog.jetbrains.com/idea/2018/09/whats-new-in-intellij-idea-2018-3-eap2/)
* GUI design tool: [Scene Builder](https://gluonhq.com/products/scene-builder/)

## Future Work
* Design better GUI.
* Show necessary information in Client/Server windows, such as ip, ports.
* Use `while` loop to listen to the server's response in `ClientListener`.
* Remove unnecessary `System.out.println()`s designed for debugging.