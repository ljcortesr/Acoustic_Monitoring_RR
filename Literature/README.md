# Corpus of the Historical Analysis

This is the annotated corpus of our historical analysis of machine listening. It gathers the 62 documents we read closely to trace how machine listening came to frame its task as *extracting useful information* from environmental sounds, and how the dataset and model at the center of our study, AudioSet and YAMNet, came to define listening as they do.

The corpus is not the product of a systematic review. It started with a detailed reading of the foundational papers that accompanied the tools we were using, and then followed two threads: one opened by our questions and our use of the app, and another following the citations of those papers, and the citations of their citations. We acknowledge that the corpus is mostly Western-centric, as it initially aimed at understanding the *current* dominant discourse of machine listening, itself grounded in Western modes. Yet we sought to include alternative approaches to sound, Indigenous ones among them, on which we rely as counterpoints.

## How we read it

We read each document closely, highlighting and annotating the passages that addressed four questions:

1. How is sound represented and transduced?
2. Who or what is figured as the listener?
3. What counts as success?
4. Whose interests are at stake?

We paid particular attention to the vocabulary the documents use to describe listening, such as "typical listener," "raters," "neutral," or "brute force." Arranging the annotated passages chronologically, we traced a sequence of transductions of sound (as inscription, signal, image, and data), summarized in the chronology below.

## A chronology of transductions

| When              | What happened                                                                                                                                                                                                                                                            | Thread                  | Corpus documents                                                |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------------------- | --------------------------------------------------------------- |
| Late 19th century | Captured as material inscriptions, sound's traces become writable, portable, comparable, and thus controllable; sound reproduction becomes a commodity                                                                                                                   | Inscription             | Latour 2012; Sterne 2003                                        |
| 1910s–1920s       | With telephony, AT&T studies the limits of human hearing to decide which features of the signal are essential for intelligibility, informing compression designed to reduce bandwidth                                                                                    | Signal                  | Sterne 2012                                                     |
| 1937              | Stevens, Volkmann, and Newman propose the logarithmic *mel* scale, still used in the log-mel spectrogram the app computes                                                                                                                                                | Signal → image          | Chion 2016                                                      |
| 1940s             | Bell Telephone Laboratories develop the sound spectrograph for military and industrial ends: analyzing scrambled speech and pursuing the phonetic typewriter                                                                                                             | Image                   | Feaster 2018; Kahn 1967; Li & Mills 2019; Parker & Dockray 2023 |
| 1953              | Cherry's experiments on attending to one voice among others (the cocktail-party effect)                                                                                                                                                                                  | Human listening         | Cherry 1953                                                     |
| 1958; 1986        | The perceptron and the backpropagation of errors anchor the statistical methods of machine learning                                                                                                                                                                      | Data                    | Rosenblatt 1958; Rumelhart et al. 1986                          |
| 1950s–1960s       | Template matching on spectrograms (Bell Labs' Audrey) proves impractical; by the late 1960s, Pierce's critique catalyzes a freeze in ASR                                                                                                                                 | Image                   | Pieraccini 2012; Pierce 1969; Li 2017                           |
| 1970s             | The World Soundscape Project names the *soundscape* and *schizophonia*, the severing of sounds from their original circumstances                                                                                                                                         | Human listening         | Schafer & Truax 1973; Schafer 1994                              |
| 1979              | VanDerveer reframes environmental sounds not as noise but as information: listeners describe them by their sources                                                                                                                                              | Human listening         | VanDerveer 1979                                                 |
| 1986–1993         | Gaver brings everyday sounds into HCI with auditory icons, and proposes an ecological account of everyday listening as distinct from musical listening, akin to VanDerveer's                                                                                | Human listening         | Gaver 1986, 1993a, 1993b                                        |
| 1990              | Bregman's auditory scene analysis explains how listeners group sound into streams; it lives on in computing as computational auditory scene analysis (CASA), which approaches sound in general rather than the specific categories of speech and music | Human listening         | Bregman 1990, 2005                                              |
| 1991              | Rowe names the analytic layer of an interactive music system "machine listening"                                                                                                                                                                                         | Machine listening named | Rowe 1991; Parker & Dockray 2023                                |
| 1995              | Miller describes WordNet, a lexical database of hierarchical relations between English words                                                                                                                                                                             | Data (text)             | Miller 1995                                                     |
| 1996              | Ellis warns auditory research against reducing sound to static images                                                                                                                                                                                                    | Image (critique)        | Ellis 1996                                                      |
| Late 2000s        | ImageNet organizes over 14 million human-annotated images into roughly 20,000 categories that follow WordNet                                                                                                                                                             | Data (image)            | Deng et al. 2009; Denton et al. 2021                            |
| 2010              | Ellis defines machine listening as the study of sound to extract useful information from it                                                                                                                                                                              | Data                    | Ellis 2010                                                      |
| 2012              | The deep CNN AlexNet wins ImageNet's recognition challenge, consolidating a data- and GPU-centric paradigm                                                                                                                                                               | Data                    | Mundlamuri et al. 2025                                          |
| 2017              | Google Research releases AudioSet, inspired by ImageNet: nearly two million hand-annotated ten-second YouTube segments in 527 categories, with convolutional baselines that classify its spectrograms as images                                                          | Data + image            | Gemmeke et al. 2017; Hershey et al. 2017; Howard et al. 2017    |
| 2020              | YAMNet, a MobileNet trained on AudioSet that scores 521 classes and runs on a phone                                                                                                                                                                                      | Data + image            | Plakal & Ellis 2020; Kosmin 2025                                |
| 2022–2025         | YouTube's terms of service and AI-training policies govern the uploads from which AudioSet's clips come                                                                                                                                                                  | Data                    | YouTube 2022, 2025                                              |

Below, each entry of the corpus gives the reference, a one- to two-sentences description/annotation. 

### 1. Technical documentation of the tools and their predecessors (13)

*Role in the analysis:* seed documents; how sound, listeners, and success are defined by the tools we used.

- **Gemmeke, J. F., et al. (2017).** *Audio Set: An Ontology and Human-Labeled Dataset for Audio Events.* IEEE International Conference on Acoustics, Speech and Signal Processing (ICASSP). [doi:10.1109/ICASSP.2017.7952261](https://doi.org/10.1109/ICASSP.2017.7952261)

  Introduces AudioSet: a hierarchical ontology of sound events and nearly two million ten-second YouTube segments labeled by human raters, explicitly inspired by ImageNet. This seed document accounts for the construction of the dataset and for the epistemological and ontological assumptions embedded in it.

- **Sound Understanding Group, Google Research (2017).** *AudioSet: A Large-Scale Dataset of Manually Annotated Audio Events* (website and ontology). [research.google.com/audioset](https://research.google.com/audioset/ontology/sounds_of_things_1.html)

  The dataset's public site, with its browsable ontology and the definition and example clips of each class. In our analysis, it is the source of the class definitions we quote.

- **Google Research (2017).** *AudioSet › Dataset › Silence.* [research.google.com/audioset/eval/silence.html](https://research.google.com/audioset/eval/silence.html)

  AudioSet's page for the "Silence" class, defined as "the absence of audible sound or the presence of sounds of very low intensity," with 7,662 segments totaling 21.2 hours. We ask, "absence of audible sound for whom, or for what?"; inspection suggested that these clips are largely moments when the audio track drops to near-zero volume.

- **Hershey, S., et al. (2017).** *CNN Architectures for Large-Scale Audio Classification.* ICASSP. [research.google/pubs](https://research.google/pubs/cnn-architectures-for-large-scale-audio-classification/)

  Evaluates image-classification architectures on spectrograms of YouTube audio, the convolutional lineage that leads to YAMNet. Here sound is classified as an image, and the *Evaluation* section measures success as agreement with a dataset's labels (AUC, mAP), not as anything resembling listening.

- **Plakal, M., & Ellis, D. (2020).** *YAMNet.* GitHub, TensorFlow Models. [github.com/tensorflow/models](https://github.com/tensorflow/models/tree/master/research/audioset/yamnet)

  Code and documentation of YAMNet, a MobileNet-based classifier trained on AudioSet that scores 521 classes, and the model our app runs. Another seed document: its account of the front end (16 kHz audio, log-mel spectrograms, 0.96-second patches) is the basis of our description of the pipeline.

- **Kosmin, D. (2025).** *KosminD/YAMNet_transfer.* GitHub. [github.com/KosminD/YAMNet_transfer](https://github.com/KosminD/YAMNet_transfer)

  A public repository for transfer learning with YAMNet. It documents why YAMNet drops some of AudioSet's 527 classes (the male/female voice distinctions, "Battle cry," and "Funny music"): as "potentially offensive mislabelings."

- **Howard, A. G., et al. (2017).** *MobileNets: Efficient Convolutional Neural Networks for Mobile Vision Applications.* arXiv. [doi:10.48550/arXiv.1704.04861](https://doi.org/10.48550/arXiv.1704.04861)

  A lightweight convolutional architecture for mobile and embedded vision. YAMNet is built on it, which makes the model's visual ancestry explicit: it descends from the image classifiers that followed AlexNet.

- **Deng, J., et al. (2009).** *ImageNet: A Large-Scale Hierarchical Image Database.* IEEE Conference on Computer Vision and Pattern Recognition (CVPR). [doi:10.1109/CVPR.2009.5206848](https://doi.org/10.1109/CVPR.2009.5206848)

  An image database that populates WordNet's hierarchy with human-annotated images. AudioSet was explicitly modeled on it, and through it WordNet's textual categories reached audio.

- **Miller, G. A. (1995).** *WordNet: A Lexical Database for English.* Communications of the ACM. [doi:10.1145/219717.219748](https://doi.org/10.1145/219717.219748)

  A lexical database that organizes English words into sets of synonyms linked by hierarchical relations. We read it as the textual root of the genealogy: ImageNet's categories follow its structure, and AudioSet inherits this ancestry.

- **Fonseca, E., et al. (2022).** *FSD50K: An Open Dataset of Human-Labeled Sound Events.* IEEE/ACM Transactions on Audio, Speech, and Language Processing. [doi:10.1109/TASLP.2021.3133208](https://doi.org/10.1109/TASLP.2021.3133208)

  An open dataset of human-labeled sound events from Freesound, motivated in part by AudioSet not being an open dataset. It offers a point of comparison for the limits that AudioSet's corporate origin places on its openness.

- **YouTube (2022).** *YouTube Terms of Service.* [youtube.com/static?template=terms](https://www.youtube.com/static?template=terms)

  The license under which videos, the source of AudioSet's clips, are uploaded to YouTube. Its clauses rechannel uploaders' contributions into training material, without ownership, compensation, or recognition.

- **YouTube (2025).** *Your Content & Third-Party Training.* YouTube Help. [support.google.com/youtube/answer/15509945](https://support.google.com/youtube/answer/15509945?hl=en)

  A help page on whether creators allow third parties to train AI models on their videos. Read with the terms of service, it shows how creators' sounds become training data.

- **DCASE (2025).** *DCASE Community.* Detection and Classification of Acoustic Scenes and Events. [dcase.community](https://dcase.community/index)

  The home of the DCASE challenges and workshops, a central venue for benchmarking environmental sound classification. It shows how computational auditory scene analysis lives on as a set of benchmark tasks.

### 2. Histories of sound, listening, and ASR (8)

*Role in the analysis:* transductions of sound into inscription, signal, and image; military and corporate interests.

- **Sterne, J. (2003).** *The Audible Past: Cultural Origins of Sound Reproduction.* Duke University Press. [doi:10.1215/9780822384250](https://doi.org/10.1215/9780822384250)

  A necessary reading in Sound Studies. A cultural history of sound reproduction technologies and the practices of listening around them. It traces how sound became a commodity by the late nineteenth century, and reminds us that the line between sound and not-sound has been drawn according to the understood capacities of human hearing.

- **Sterne, J. (2012).** *MP3: The Meaning of a Format.* Duke University Press. [read.dukeupress.edu](https://read.dukeupress.edu/books/book/1663/MP3The-Meaning-of-a-Format)

  A hundred-year history of the MP3 format and of perceptual coding. Its account of how AT&T studied and exploited the limits of human hearing to compress the telephone signal led us to the normative ear behind what we call the universal listener.

- **Li, X. (2017).** *Divination Engines: A Media History of Text Prediction.* PhD dissertation. [ProQuest](https://www.proquest.com/docview/1964286735/abstract/E7A6927BCBA7489DPQ/1)

  A history of computational text prediction in the second half of the twentieth century, including statistical speech recognition. It documents the *brute-force* turn, in which IBM abandoned the modeling of human listening for "a natural way for the machine to do it," as well as Pierce's skepticism toward speech recognition.

- **Li, X., & Mills, M. (2019).** *Vocal Features: From Voice Identification to Speech Recognition by Machine.* Technology and Culture. [Project MUSE](https://muse.jhu.edu/pub/1/article/727301)

  A history of machine methods for collecting and analyzing voice recordings between 1908 and 1970. From it, we followed the spectrograph and the long-envisioned phonetic typewriter.

- **Pieraccini, R. (2012).** *The Voice in the Machine: Building Computers That Understand Speech.* MIT Press. [direct.mit.edu](https://direct.mit.edu/books/monograph/2945/The-Voice-in-the-MachineBuilding-Computers-That)

  A practitioner's history of more than sixty years of speech recognition. It recounts why template matching on spectrograms (Bell Labs' Audrey) proved impractical, and how computational intensity became a profitable feature for a hardware company. Inspiring from a critical technical practice perspective, as Pieraccini was first and foremost a practitioner. 

- **Feaster, P. (2018).** *The Secret Military Origins of the Sound Spectrograph.* Griffonage-Dot-Com (blog). [griffonagedotcom.wordpress.com](https://griffonagedotcom.wordpress.com/2018/07/26/the-secret-military-origins-of-the-sound-spectrograph/)

  An account of the wartime development of the sound spectrograph at Bell Labs. It grounds the military origins of the spectrogram, the image that YAMNet classifies.

- **Kahn, D. (1967).** Censors, Scramblers, and Spies. In *The Codebreakers.* Macmillan.

  A chapter on wartime censorship, speech scrambling, and its interception. Read alongside Feaster, it situates the analysis of scrambled speech in its military context.

- **Latour, B. (2012).** *Visualisation and Cognition: Drawing Things Together.* Avant: Trends in Interdisciplinary Studies (reprint of the 1986 essay).

  An essay on inscriptions that make the world writable, portable, and comparable. It helped us think about how sound's traces, once captured as material inscriptions, became controllable.

### 3. Genealogies and critiques of AI and datasets (10)

*Role in the analysis:* dataset lineage from WordNet and ImageNet; benchmarks; classification as politics.

- **Denton, E., et al. (2021).** *On the Genealogy of Machine Learning Datasets: A Critical History of ImageNet.* Big Data & Society. [doi:10.1177/20539517211035955](https://doi.org/10.1177/20539517211035955)

  A critical history of the norms, values, and assumptions built into ImageNet. It led us to see AudioSet's inherited epistemological bias toward written representations of the world, which diminishes the sensory.

- **Crawford, K., & Joler, V. (2018).** *Anatomy of an AI System.* [anatomyof.ai](http://www.anatomyof.ai)

  An essay and map of the human labor, data, and planetary resources behind the Amazon Echo. It reminds us of the material infrastructure behind voice-based machine listening.

- **Crawford, K., & Paglen, T. (2021).** *Excavating AI: The Politics of Images in Machine Learning Training Sets.* AI & Society. [doi:10.1007/s00146-021-01162-8](https://doi.org/10.1007/s00146-021-01162-8)

  An examination of the categories and images in training sets such as ImageNet. It shows how training sets encode the categories, labor, and interests of their makers.

- **Crawford, K. (2021).** *Atlas of AI: Power, Politics, and the Planetary Costs of Artificial Intelligence.* Yale University Press.

  An account of AI as an extractive industry of labor, data, and natural resources. It made us attentive to the word *pipeline*, which originated in the oil and gas industry and has extended into extractive AI.

- **Paullada, A., et al. (2021).** *Data and Its (Dis)contents: A Survey of Dataset Development and Use in Machine Learning Research.* Patterns. [doi:10.1016/j.patter.2021.100336](https://doi.org/10.1016/j.patter.2021.100336)

  A survey of how datasets are built and used in machine learning research, and of their limits. It supports our reading of corporate datasets and pre-trained models as *de facto* benchmarks.

- **Raji, I. D., et al. (2021).** *AI and the Everything in the Whole Wide World Benchmark.* arXiv. [doi:10.48550/arXiv.2111.15366](https://doi.org/10.48550/arXiv.2111.15366)

  A critique of benchmarks that claim to measure general capabilities. It helped us question claims to generality built on categories drawn from English-language web content.

- **Mundlamuri, R., et al. (2025).** *The Evolution of AI: From Classical Machine Learning to Modern Large Language Models.* IEEE Access. [doi:10.1109/ACCESS.2025.3621344](https://doi.org/10.1109/ACCESS.2025.3621344)

  A survey of AI's development from classical machine learning to large language models. We draw on it for the moment in 2012 when AlexNet consolidated a data- and GPU-centric paradigm.

- **Kang, E. B. (2023).** *Ground Truth Tracings (GTT): On the Epistemic Limits of Machine Learning.* Big Data & Society. [doi:10.1177/20539517221146122](https://doi.org/10.1177/20539517221146122)

  A method for tracing how ground truth is constructed, and an argument about the epistemic limits of machine learning. It underlines that the institutions that build canonical datasets decide what machines learn.

- **DeVries, T., et al. (2019).** *Does Object Recognition Work for Everyone?* arXiv. [doi:10.48550/arXiv.1906.02659](https://doi.org/10.48550/arXiv.1906.02659)

  Shows that commercial object-recognition systems perform worse on images from lower-income households and from outside the West. It made us wary of categories nameable in English being presented as universal concepts.

- **Bowker, G. C., & Star, S. L. (1999).** *Sorting Things Out: Classification and Its Consequences.* MIT Press. [direct.mit.edu](https://direct.mit.edu/books/monograph/4738/Sorting-Things-OutClassification-and-Its)

  A study of how classification systems shape worldviews and social life. It frames classification as politics, and benchmarks as infrastructure.

### 4. Scholarship on machine listening beyond voice (6)

*Role in the analysis:* definitions of machine listening; its military, industrial, and ecological expansion; listening labor.

- **Sterne, J. (2022).** *Is Machine Listening Listening?* communication +1. [doi:10.7275/zeqh-eg38](https://doi.org/10.7275/zeqh-eg38)

  An essay asking whether the processing of sound by machines counts as listening. Its description of machine listening as turning sound into data and subjecting that data to machine learning is part of our working definition.

- **Parker, J. E. K., & Dockray, S. (2023).** *'All Possible Sounds': Speech, Music, and the Emergence of Machine Listening.* Sound Studies. [doi:10.1080/20551940.2023.2195057](https://doi.org/10.1080/20551940.2023.2195057)

  A history of machine listening as a field, from speech and music research between the 1940s and 1990s to the ambition of machines that hear "all possible sounds." It traces the origin of the term in interactive computer music, as well as the phonetic typewriter and CASA.

- **Parker, J. E. K. (2025).** *The Planetization of Machine Listening.* Critical Inquiry. [doi:10.1086/737056](https://doi.org/10.1086/737056)

  A critical account of machine listening turned on the environment for conservation. It warns of the "planetization" of audio AI, in a domain already dependent on big tech infrastructure.

- **Semel, B. M. (2022).** *Listening Like a Computer: Attentional Tensions and Mechanized Care in Psychiatric Digital Phenotyping.* Science, Technology, & Human Values. [doi:10.1177/01622439211026371](https://doi.org/10.1177/01622439211026371)

  An ethnography of the human listening work behind psychiatric voice analysis. It brings the listening labor behind machine listening into view, and keeps open the question of whether machine listening is listening.

- **Napolitano, D., & Grieco, R. (2021).** *The Folded Space of Machine Listening.* SoundEffects. [doi:10.7146/se.v10i1.124205](https://doi.org/10.7146/se.v10i1.124205)

  A media-archaeological study of acoustic fingerprinting and audio watermarking algorithms. It holds that machine and human listening are distinct but co-defined forms.

- **Blazsek, A. (2024).** *Machine Listening as Sonification.* Organised Sound. [doi:10.1017/S1355771824000104](https://doi.org/10.1017/S1355771824000104)

  An argument for understanding machine listening as a form of sonification. It takes part in the debate over whether the algorithmic processing of sonic signals is listening at all.

### 5. Primary sources on the perception and computation of environmental sound (13)

*Role in the analysis:* sound-as-information; source-based description; the shift to brute force; time as a dimension.

- **VanDerveer, N. J. (1979).** *Ecological Acoustics: Human Perception of Environmental Sounds.* PhD dissertation. [ProQuest](https://www.proquest.com/docview/302934163/citation/B27863EA4DBD467DPQ/1)

  Experiments in which listeners wrote down what they heard in sequences of common environmental sounds. VanDerveer reframed these sounds from noise to information, and found that listeners grasp them less through their acoustic properties than through their sources, the source-based description that AudioSet inherits.

- **Bregman, A. S. (1990).** *Auditory Scene Analysis: The Perceptual Organization of Sound.* MIT Press. [direct.mit.edu](https://direct.mit.edu/books/monograph/3887/Auditory-Scene-AnalysisThe-Perceptual-Organization)

  A theory of how listeners organize complex sound environments into perceptual streams. It is the root of computational auditory scene analysis (CASA), and it helped us see that environmental sound lacks an atomic unit equivalent to the phoneme or the note.

- **Bregman, A. S. (2005).** *Auditory Scene Analysis and the Role of Phenomenology in Experimental Psychology.* Canadian Psychology. [doi:10.1037/h0085822](https://doi.org/10.1037/h0085822)

  A reflection on subjectivity in psychological research, supported by audio demonstrations of auditory organization. Its accounts how sound is grouped into streams by spectral and temporal similarity and continuity.

- **Gaver, W. W. (1986).** *Auditory Icons: Using Sound in Computer Interfaces.* Human–Computer Interaction. [doi:10.1207/s15327051hci0202_3](https://doi.org/10.1207/s15327051hci0202_3)

  Proposes auditory icons, short everyday sounds used as interface feedback. It exemplifies the early, applied uses of environmental sound in HCI.

- **Gaver, W. W. (1993a).** *What in the World Do We Hear? An Ecological Approach to Auditory Event Perception.* Ecological Psychology. [doi:10.1207/s15326969eco0501_1](https://doi.org/10.1207/s15326969eco0501_1)

  An ecological account of everyday listening as hearing events in the world, distinct from musical listening. It shows the source-based grammar of environmental sound, and its question, "what do we hear in the world?", is one we propose to revisit through the materialities and conditions of sounding.

- **Gaver, W. W. (1993b).** *How Do We Hear in the World? Explorations in Ecological Acoustics.* Ecological Psychology. [doi:10.1207/s15326969eco0504_2](https://doi.org/10.1207/s15326969eco0504_2)

  A pair paper with the previous one. An exploration of the acoustic basis of everyday listening. It treats time as a salient dimension of sound, one that convolutional classifiers compress into just another spatial axis.

- **Cherry, E. C. (1953).** *Some Experiments on the Recognition of Speech, with One and with Two Ears.* Journal of the Acoustical Society of America. [doi:10.1121/1.1907229](https://doi.org/10.1121/1.1907229)

  Experiments on how listeners separate two simultaneous speech messages, behind what came to be called the cocktail-party effect: holding attention on one sound while the rest recede into the background.

- **Pierce, J. R. (1969).** *Whither Speech Recognition?* Journal of the Acoustical Society of America. [doi:10.1121/1.1911801](https://doi.org/10.1121/1.1911801)

  A skeptical assessment of speech recognition research and its funding, which catalyzed a freeze in ASR in the late 1960s. Pierce's verdict: "The work has been an experience, not an experiment."

- **Rowe, R. J. (1991).** *Machine Listening and Composing: Making Sense of Music with Cooperating Real-Time Agents.* PhD thesis, MIT. [hdl.handle.net/1721.1/13835](http://hdl.handle.net/1721.1/13835)

  An interactive music system whose analytic layer listens to live performers in real time. It is an early use of the term *machine listening*, and it proposed to *coach* rather than train the system, modeling its analysis on human ways of listening.

- **Ellis, D. P. W. (1996).** *Prediction-Driven Computational Auditory Scene Analysis.* PhD thesis, MIT. [doi:10.7916/D84J0N13](https://doi.org/10.7916/D84J0N13)

  A computational model of auditory scene analysis driven by prediction. It explains how spectrograms moved listening into the visual domain, where computational methods were more developed, while warning against the visual field's temptation to "simplify" problems to static images.

- **Ellis, D. (2010).** *A History and Overview of Machine Listening.* Columbia University. [PDF](https://scispace.com/pdf/a-history-and-overview-of-machine-listening-52t37dau1h.pdf)

  A practitioner's overview of the history and scope of machine listening. It gives the field's own definition, extracting useful information from sound, which we adopt as a starting point and question throughout.

- **Rosenblatt, F. (1958).** *The Perceptron: A Probabilistic Model for Information Storage and Organization in the Brain.* Psychological Review. [doi:10.1037/h0042519](https://doi.org/10.1037/h0042519)

  The perceptron, a model of learning in a hypothetical nervous system. It is an early landmark in the statistical lineage of machine learning.

- **Rumelhart, D. E., Hinton, G. E., & Williams, R. J. (1986).** *Learning Representations by Back-Propagating Errors.* Nature. [doi:10.1038/323533a0](https://doi.org/10.1038/323533a0)

  Backpropagation, the learning procedure for multi-layer networks. Its resurgence prepared the statistical methods that later became deep learning.

### 6. Foundational sound studies (7)

*Role in the analysis:* soundscape and its critiques; schizophonia; silence; the mel scale. Some of the works summarized in previous categories, such as Sterne, 2003, are not repeated here. 

- **Schafer, R. M., & Truax, B. (1973).** *The World Soundscape Project.* Simon Fraser University. [sfu.ca/~truax/wsp.html](https://www.sfu.ca/~truax/wsp.html)

  The research group that founded soundscape studies and acoustic ecology. From it come the *soundscape* and *schizophonia*, the severing of sounds from their original circumstances, which we find in AudioSet's clips cut from YouTube videos.

- **Schafer, R. M. (1994).** *The Soundscape: Our Sonic Environment and the Tuning of the World.* Destiny Books.

  The book that coined *soundscape*, first published in 1977 as *The Tuning of the World*. It defines the soundscape as the relationship between listeners and the sonic environment they inhabit.

- **Corbin, A. (1999).** *Village Bells: Sound and Meaning in the 19th-Century French Countryside.* Papermac.

  A history of the meanings of bells in rural France. It shows the historical contingency that the notion of soundscape tends to flatten.

- **Kelman, A. Y. (2010).** *Rethinking the Soundscape: A Critical Genealogy of a Key Term in Sound Studies.* The Senses and Society. [doi:10.2752/174589210X12668381452845](https://doi.org/10.2752/174589210X12668381452845)

  A critical genealogy of the term *soundscape* and its misapplications. It exposes the normative hierarchies behind the term.

- **Ingold, T. (2007).** *Against Soundscape.* In *Autumn Leaves: Sound and the Environment in Artistic Practice.* [abdn.elsevierpure.com](https://abdn.elsevierpure.com/en/publications/against-soundscape/)

  An argument that sound is not an object of perception but the medium in which we perceive. It is one of the critiques of the soundscape we draw on.

- **Chion, M. (2016).** *Sound: An Acoulogical Treatise.* Duke University Press.

  A treatise on sound as an object of listening and of description. It offers grammars for describing sound that reach beyond its sources, an account of the mel scale, and the idea of listening as being "touched" at a distance.

- **Ochoa Gautier, A. M. (2015).** *Silence.* In *Keywords in Sound.* Duke University Press. [read.dukeupress.edu](https://read.dukeupress.edu/books/book/166/Keywords-in-Sound)

  An essay on silence as relative and culturally situated. It helped us question AudioSet's "Silence" category, with its single, absolute absence of sound.

### 7. Alternative -including Indigenous- approaches to sound and listening (4)

*Role in the analysis:* counterpoints to the dominant discourse, taken up in the paper's discussion.

- **Robinson, D. (2020).** *Hungry Listening: Resonant Theory for Indigenous Sound Studies.* University of Minnesota Press.

  Considers listening from both Indigenous and settler colonial perspectives, and names the extractive "hungry listening" of Western systems. It informs three of our arguments: that listening sits at the intersection of perceptual habit, ability, and bias; that knowledge systems can be conjoined rather than merged; and that listening can be sovereign.

- **Feld, S. (2015).** *Acoustemology.* In *Keywords in Sound.* Duke University Press. [read.dukeupress.edu](https://read.dukeupress.edu/books/book/166/Keywords-in-Sound)

  An essay on *acoustemology*, sound as a way of knowing. It describes how we make sense of acoustic sensations through local conditions, the interplay of the senses, culture, and memory.

- **Feld, S. (2012).** *Sound and Sentiment: Birds, Weeping, Poetics, and Song in Kaluli Expression* (3rd ed.). Duke University Press.

  A sensory ethnography of sound among the Kaluli people of Bosavi, Papua New Guinea. In its new introduction, Feld describes sound as "both a capacity to know and a habit of knowing."

- **Feld, S. (1995).** *Waterfalls of Song.* In *Senses of Place.*

  An essay on sound, place, and knowing in Bosavi. It describes listening as a holistic engagement that binds the whole body to the experience of place.

### 8. HCI reviews (1)

*Role in the analysis:* the place of environmental sound in HCI.

- **Johansen, S. S., van Berkel, N., & Fritsch, J. (2022).** *Characterising Soundscape Research in Human-Computer Interaction.* ACM Designing Interactive Systems Conference (DIS). [doi:10.1145/3532106.3533458](https://doi.org/10.1145/3532106.3533458)

  A review of soundscape research in HCI. It shows that HCI research on environmental sound remains predominantly applied, the gap our paper addresses.
