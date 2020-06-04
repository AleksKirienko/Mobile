using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PlayerController : MonoBehaviour {
    public float speed;
    private Rigidbody rb;

	public Text Life, winText, Timer;
    private float time = 0.0f;
    private int count;

    void Start() {
    	Debug.Log("Start() = " + Time.time);
        rb = GetComponent<Rigidbody>();
        count = 3;
        winText.text = "";
        setTimer();
        setCount();
    }

    void FixedUpdate() {

        setTimer();
        float moveHorizontal = Input.GetAxis("Horizontal");

        Vector3 movement = new Vector3(moveHorizontal, 0.0f, 0.0f);

        rb.AddForce(movement * speed);

    }

    void OnTriggerEnter (Collider other) {
        if(other.tag == "Ball") {
            Destroy(other.gameObject);
            count--;
            setCount();
        }   
    }
 
    void setCount() {
        Life.text = "Life: " + count.ToString();
        
        if(count == 0) winText.text = "Game over!";


    }

    void setTimer() {
        time += Time.deltaTime;
        if(count != 0) {
            Timer.text = time.ToString();  
            if (time >= 10.0) winText.text = "You win!";
        }

    }

}
