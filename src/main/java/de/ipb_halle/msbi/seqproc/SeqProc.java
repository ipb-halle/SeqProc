package de.ipb_halle.msbi.seqproc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.WindowConstants;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.FastaWriterHelper;
import org.apache.commons.io.FilenameUtils;

public class SeqProc {
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("SeqProc");
		final javax.swing.JTextArea text = new javax.swing.JTextArea();
		frame.getContentPane().add(new javax.swing.JScrollPane(text), java.awt.BorderLayout.CENTER);

		new FileDrop(null, text, new FileDrop.Listener() {
			public void filesDropped(java.io.File[] Files) {
				for (int i = 0; i < Files.length; i++) {
					try {
						text.append("Processing " + Files[i].getCanonicalPath() + "\n");
						LinkedHashMap<String, DNASequence> DNASequences = FastaReaderHelper.readFastaDNASequence(Files[i]);
						String ResultFileName = FilenameUtils.removeExtension(Files[i].getCanonicalPath()) + ".processed.fna";
						ArrayList<DNASequence> DNASequencesSplited = new ArrayList<DNASequence>();
						
						for (String key : DNASequences.keySet())
						{
							text.append("\tProcessing sequence " + key + "\n");
							DNASequence InputSeq = DNASequences.get(key);
							Integer Length = InputSeq.getLength();

							DNASequence SubSeq1 = new DNASequence("GAAGACAAA"+InputSeq.getSubSequence(1, Length/2+4).getSequenceAsString()+"GTCTTC");
							DNASequence SubSeq2 = new DNASequence("GAAGAC"+InputSeq.getSubSequence(Length/2+1-4, Length).getSequenceAsString()+"GCTTTTGTCTTC");

							SubSeq1.setOriginalHeader(InputSeq.getOriginalHeader()+".Frag1");
							SubSeq2.setOriginalHeader(InputSeq.getOriginalHeader()+".Frag2");
							DNASequencesSplited.add(SubSeq1);
							DNASequencesSplited.add(SubSeq2);
						}
						
						text.append("Writing results to " +  ResultFileName + "\n");
						FastaWriterHelper.writeNucleotideSequence(new File(ResultFileName),DNASequencesSplited);
						text.append("Finished " + Files[i].getCanonicalPath() + "\n");
					} // end try
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} // end for: through each dropped file
			} // end filesDropped
		}); // end FileDrop.Listener

		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	} // end main

}
