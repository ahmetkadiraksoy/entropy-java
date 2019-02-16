import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main (String args[]) {
		ArrayList<String> examples = new ArrayList<>();

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(args[0]));

			while ((sCurrentLine = br.readLine()) != null) {
				examples.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		System.out.println(find_entropies(examples, (examples.get(0).split(",").length - 1)));
	}

	public static double find_entropies(ArrayList<String> examples, int number_of_attributes)
	{
		// Find the number of unique classes
		ArrayList<String> unique_classes = new ArrayList<>();
		for (int i = 0; i < examples.size(); i++)
		{
			String example_class = examples.get(i).split(",")[number_of_attributes]; // get the class from example j
			boolean exists = false;
			for (int j = 0; j < unique_classes.size(); j++)
			{
				if (unique_classes.get(j).equals(example_class))
				{
					exists = true;
					break;
				}
			}

			if (!exists)
				unique_classes.add(example_class);
		}
		int no_of_classes = unique_classes.size();

		//ArrayList<String> entropies = new ArrayList<>(); // holds entropy values for features
		double average_entropy = 0;

		for (int i = 0; i < number_of_attributes; i++) // for each attribute
		{
			ArrayList<test> instances = new ArrayList<>(); // holds unique instances for each attribute
			ArrayList<ArrayList<test>> instance_classes = new ArrayList<>(); // holds unique classes for each attribute

			for (int j = 0; j < examples.size(); j++) // for each example
			{
				String example_attribute = examples.get(j).split(",")[i]; // get the attribute value from example j
				String example_class = examples.get(j).split(",")[number_of_attributes]; // get the class from example j

				// if instance does not exist in list, add it. if exists, increment occurance
				boolean instance_exists = false;
				for (int k = 0; k < instances.size(); k++) // loop through the instances
				{
					if (instances.get(k).text.equals(example_attribute)) // if current instance is found
					{
						instance_exists = true;
						instances.get(k).occurance++; // increment the instance occurance

						// If the class of the current example exists, increment it
						boolean class_exists = false;
						for (int l = 0; l < instance_classes.get(k).size(); l++)
						{
							if (instance_classes.get(k).get(l).text.equals(example_class))
							{
								class_exists = true;
								ArrayList<test> item = instance_classes.get(k);
								item.get(l).occurance++;
								instance_classes.set(k, item);

								break;
							}
						}

						// If the class of the current example does not exist, add it
						if (!class_exists)
						{
							ArrayList<test> item = instance_classes.get(k);
							test test2 = new test();
							test2.text = example_class;
							item.add(test2);
							instance_classes.set(k, item);
						}

						break;
					}
				}

				if (!instance_exists)
				{
					test test2 = new test();
					test2.text = example_attribute;
					instances.add(test2); // add instance

					// Add class
					test2 = new test();
					test2.text = example_class;

					ArrayList<test> item = new ArrayList<>();
					item.add(test2);
					instance_classes.add(item);
				}
			}

			double entropy = 0;
			for (int j = 0; j < instances.size(); j++)
			{
				double without_rate = 0;
				for (int k = 0; k < instance_classes.get(j).size(); k++)
				{
					double term = instance_classes.get(j).get(k).occurance/((double)instances.get(j).occurance);
					without_rate = without_rate + ((-1) * term * (Math.log(term)/Math.log(no_of_classes)));
				}
				without_rate = without_rate * (instances.get(j).occurance/((double)examples.size()));
				entropy = entropy + without_rate;
			}
			//entropies.add(Double.toString(entropy));
			average_entropy += entropy;
		}

		return (1 - average_entropy/number_of_attributes);
	}

}

class test {
	String text = "";
	int occurance = 1;
}
