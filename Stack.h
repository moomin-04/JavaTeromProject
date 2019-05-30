class Stack	{
	public:
		Stack(int s=10); // default constructor (stack size 10)
		//destructor
		~Stack(){delete[] ptr;}
		void push(int v);
		int pop();
	protected:
		//determine whether Stack is empty
		bool isEmpty();
		//determine whether Stack is full
		bool isFull();
	private:
		int size;	//# of elements in the stack
		int top; 	//location of the top element
		int *ptr;	//pointer to the stack
};// end class Stack

Stack::Stack(int s){
	size=s>0?s:10;
	top=-1;
	ptr=new int[size];
}
void Stack::push(int v){
	if(!isFull()){
		ptr[++top]=v;
	}//end if
}

int Stack::pop(){
	if(!isEmpty()){
		return ptr[top--];
	}
	exit(1);
}

bool Stack::isFull(){
	if(top>=size-1) return true;
	else return false;
}

boll Stack::isEmpty(){
	if(top==-1) return true;
	else return false;
}