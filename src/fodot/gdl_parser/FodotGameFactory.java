package fodot.gdl_parser;

import static fodot.objects.FodotElementBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fodot.objects.structure.elements.predicateenum.FodotPredicateEnumeration;
import fodot.objects.structure.elements.predicateenum.elements.FodotPredicateBooleanEnumerationElement;
import org.ggp.base.util.Pair;

import fodot.gdl_parser.second_phase.GdlFodotTransformer;
import fodot.gdl_parser.second_phase.data.FodotCompoundData;
import fodot.gdl_parser.second_phase.data.FodotNextData;
import fodot.objects.comments.FodotComment;
import fodot.objects.file.IFodotFile;
import fodot.objects.file.IFodotFileElement;
import fodot.objects.general.IFodotElement;
import fodot.objects.includes.FodotIncludeHolder;
import fodot.objects.procedure.FodotProcedureStatement;
import fodot.objects.procedure.FodotProcedures;
import fodot.objects.structure.FodotStructure;
import fodot.objects.structure.elements.predicateenum.elements.IFodotPredicateEnumerationElement;
import fodot.objects.termdefinition.FodotTermDefinition;
import fodot.objects.theory.FodotTheory;
import fodot.objects.theory.elements.formulas.FodotPredicate;
import fodot.objects.theory.elements.formulas.IFodotFormula;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveFunction;
import fodot.objects.theory.elements.inductivedefinitions.FodotInductiveSentence;
import fodot.objects.theory.elements.terms.FodotFunction;
import fodot.objects.theory.elements.terms.FodotVariable;
import fodot.objects.theory.elements.terms.IFodotTerm;
import fodot.objects.vocabulary.FodotLTCVocabulary;
import fodot.objects.vocabulary.FodotVocabulary;
import fodot.objects.vocabulary.elements.FodotFunctionFullDeclaration;
import fodot.objects.vocabulary.elements.FodotPredicateDeclaration;
import fodot.objects.vocabulary.elements.FodotType;
import fodot.objects.vocabulary.elements.FodotTypeDeclaration;
import fodot.util.FormulaUtil;
import org.ggp.base.util.gdl.grammar.GdlProposition;

/**
 * @author Frederik Goovaerts <frederik.goovaerts@student.kuleuven.be>
 */
public class FodotGameFactory {

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	public FodotGameFactory(GdlFodotTransformer source,
			FodotPredicateDeclaration doPred,
			FodotPredicateDeclaration terminalPred,
			int timeLimit) {

		this.source = source;
		this.doPredicateDeclaration = doPred;
		this.terminalTimePredicateDeclaration = terminalPred;
		this.turnLimit = timeLimit;
		buildDefaultVocItems();
	}

	public FodotGameFactory(GdlFodotTransformer source,
			FodotPredicateDeclaration doPred,
			FodotPredicateDeclaration terminalPred) {
		this(source, doPred, terminalPred, DEFAULT_TURN_LIMIT);
	}

	/***************************************************************************
	 * Class Properties
	 **************************************************************************/

	private GdlFodotTransformer source;

	private FodotFunctionFullDeclaration startFunctionDeclaration;
	private FodotFunctionFullDeclaration nextFunctionDeclaration;
	private FodotPredicateDeclaration doPredicateDeclaration;
	private FodotPredicateDeclaration terminalTimePredicateDeclaration;
	private FodotTypeDeclaration scoreTypeDeclaration;
	private FodotFunctionFullDeclaration scoreFunctionDeclaration;

	private int turnLimit = DEFAULT_TURN_LIMIT;

	public final static int DEFAULT_TURN_LIMIT = 30;

	/***************************************************************************
	 * Class Methods
	 **************************************************************************/

	public IFodotFile createFodot() {
		FodotComment topComment = createComment(0,
				"This FO(.) file was generated with PlayID.\n" //TODO add from what file it was generated?
				+ "PlayID is a program made by Frederik Goovaerts and Thomas Winters.");
		FodotVocabulary voc = this.buildVocabulary();
		FodotTheory theo = this.buildTheory(voc);
		FodotStructure struc = this.buildStructure(voc);
		List<FodotTermDefinition> terms = this.buildTerms(voc);
		FodotProcedures proc = this.buildProcedures();
		FodotIncludeHolder incl = createIncludeHolder(createIncludeLTC());

		List<IFodotFileElement> elements = new ArrayList<IFodotFileElement>(Arrays.asList(topComment, voc,theo,struc));
		elements.addAll(terms);
		elements.add(proc);

		return createFodotFile(incl, elements);
	}
	

	public static final String SCORE_FUNCTION_NAME = "Score";
	public static final String NEXT_FUNCTION_NAME = "Next";
	public static final String START_CONSTANT_NAME = "Start";
	
	private void buildDefaultVocItems() {

		this.startFunctionDeclaration = createCompleteFunctionDeclaration(START_CONSTANT_NAME, source.getTimeType());

		List<FodotType> timeList = new ArrayList<>();
		timeList.add(source.getTimeType());
		this.nextFunctionDeclaration = createPartialFunctionDeclaration(NEXT_FUNCTION_NAME, timeList,
				source.getTimeType());

		this.scoreTypeDeclaration = createTypeDeclaration(source.getScoreType());

		List<FodotType> playerList = new ArrayList<>();
		playerList.add(source.getPlayerType());
		scoreFunctionDeclaration = createCompleteFunctionDeclaration(SCORE_FUNCTION_NAME, playerList,
				source.getScoreType());
	}

	private FodotVocabulary buildVocabulary() {
		FodotVocabulary toReturn = getDefaultVocabulary();

		toReturn.addElement(createBlankLines(1));
		toReturn.addElement(createComment("Vocabulary elements derived from GDL file"));

		/**
		 * nodig: alle roles
		 * resultaat:
		 * type Player constructed from {*alle roles*}
		 */
		toReturn.addElement(
				createTypeDeclaration(
						source.getPlayerType()
						)
				);


		/**
		 * Add all other found types
		 */
		for (FodotType type : source.getGdlVocabulary().getOtherTypes()) {
			toReturn.addElement(type.getDeclaration());
		}
		//        /**
		//         * nodig: alle static predicaten
		//         * resultaat:
		//         * pred(*standaard argumenten*)
		//         */
		//        for (FodotPredicateDeclaration declaration : this.pool.getStaticPredicates()) {
		//            toReturn.addElement(declaration);
		//        }
		//
		//        /**
		//         * nodig: alle compound static predicaten
		//         * resultaat:
		//         * pred(*standaard argumenten*)
		//         */
		//        for (FodotPredicateDeclaration declaration : this.pool.getCompoundTimedDeclarations()) {
		//            toReturn.addElement(declaration);
		//        }

		Set<FodotPredicateDeclaration> dynamicPredicates = source.getGdlVocabulary().getDynamicPredicates();

		/**
		 * Add all static predicates
		 */
		if (source.getGdlVocabulary().getFodotPredicates().size() > dynamicPredicates.size()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Static predicates"));
			for (FodotPredicateDeclaration declaration : source.getGdlVocabulary().getFodotPredicates()) {
				if (!dynamicPredicates.contains(declaration)) {
					toReturn.addElement(declaration);
				}
			}
		}

		/**
		 * Add all dynamic predicates
		 */
		if (dynamicPredicates.size() > 0) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Dynamic predicates"));
			toReturn.addAllElements(dynamicPredicates);


			//Add causations and initials of these dynamic predicates

			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("LTC predicates for the fluent predicates"));
			/**
			 * nodig: alle fluent predicaten
			 * resultaat voor elk predicaat:
			 * pred(Time,*standaard argumenten*)
			 * I_pred(*standaard argumenten*)
			 * C_pred(Time,*standaard argumenten*)
			 */
			for (FodotPredicateDeclaration declaration : source.getGdlVocabulary().getDynamicPredicates()) {
				if(!source.getCompoundMap().containsKey(declaration)) {
					//        	toReturn.addElement(createComment("LTC for " + declaration.getName()));
					//            toReturn.addElement(this.pool.getTimedVerionOf(declaration));
					toReturn.addElement(this.source.getInitialOf(declaration));
					toReturn.addElement(this.source.getCauseOf(declaration));
					//				toReturn.addElement(this.pool.getCauseNotOf(declaration));
					toReturn.addElement(createBlankLines(1));
				}
			}
		}

        Map<GdlProposition, FodotPredicateDeclaration> propositions =
                this.source.getGdlVocabulary().getPropositions();
        if (propositions.size() > 0) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("Propositions & causes-predicate"));
            for (FodotPredicateDeclaration declaration : propositions.values()) {
                toReturn.addElement(declaration);
                toReturn.addElement(this.source.getCauseOf(declaration));
            }
        }


		return toReturn;
	}

	private FodotTheory buildTheory(FodotVocabulary voc) {
		FodotTheory toReturn = getDefaultTheory(voc);

		/**
		 * nodig: alle fluent predicaten
		 * resultaat voor elk predicaat:
		 * {
		 *     !(var [Unfilled])*aantal argumenten keer* : *pred*(Start,*vars*) <- I_*pred*(*vars*).
		 *     !(var [Unfilled])*aantal argumenten keer* t [Time]: *pred*(Next(t),*vars*) <- C_*pred*(t,*vars*).
		 * }
		 */
		Set<FodotPredicateDeclaration> dynamicPredicates = source.getGdlVocabulary().getDynamicPredicates();
		if (!dynamicPredicates.isEmpty()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Inductive definitions for the fluent predicates"));
		}
		for (FodotPredicateDeclaration declaration : dynamicPredicates) {
			if(!source.getCompoundMap().containsKey(declaration)) {
				List<FodotInductiveSentence> definitions = new ArrayList<>();

				int originalArity = declaration.getAmountOfArgumentTypes();

				List<IFodotTerm> argList = new ArrayList<>();
				List<IFodotTerm> iArgList = new ArrayList<>();
				Set<FodotVariable> varSet = new HashSet<>();
				argList.add(createFunction(this.startFunctionDeclaration));

				for (int i = 1; i < originalArity; i++) {
					FodotVariable newVar = createVariable(declaration.getArgumentType(i), varSet);
					argList.add(newVar);
					iArgList.add(newVar);
					varSet.add(newVar);
				}

				definitions.add(
						createInductiveSentence(
								createInductiveDefinitionConnector(
										createPredicate(declaration, argList),
										createPredicate(this.source.getInitialOf(declaration), iArgList)
										//createPredicate(null, iArgList)
										)
								)
						);

				argList = new ArrayList<>(iArgList);
				List<IFodotTerm> cArgList = new ArrayList<>(iArgList);
				varSet = new HashSet<>(varSet);

				FodotVariable timeVar = createVariable(source.getTimeType(), varSet);

				argList.add(0, createFunction(this.nextFunctionDeclaration, timeVar));
				cArgList.add(0, timeVar);
				varSet.add(timeVar);

				definitions.add(createInductiveSentence(
						createInductiveQuantifier(
								createForAll(
										varSet,
										createInductiveDefinitionConnector(
												createPredicate(declaration, argList),
												createPredicate(this.source.getCauseOf(declaration), cArgList)
												//createPredicate(null,cArgList)
												)
										)
								)
						));

				toReturn.addElement(createInductiveDefinition(definitions));
			}
		}

        /**
         * nodig: alle fluent propositions
         * resultaat voor elke proposition:
         * {
         *     ! t [Time]: *proppred*(Next(t)) <- C_*proppred*(t,).
         * }
         */
        Map<GdlProposition,FodotPredicateDeclaration> propMap =
                this.source.getGdlVocabulary().getPropositions();
        if (!propMap.isEmpty()) {
            toReturn.addElement(createBlankLines(1));
            toReturn.addElement(createComment("Inductive definitions for the fluent propositions"));
        }
        for (GdlProposition prop : propMap.keySet()) {

                List<FodotInductiveSentence> definitions = new ArrayList<>();

                List<IFodotTerm> argList = new ArrayList<>();
                Set<FodotVariable> varSet = new HashSet<>();

                FodotVariable timeVar = createVariable(source.getTimeType(), varSet);

                argList.add(createFunction(this.nextFunctionDeclaration, timeVar));

                definitions.add(createInductiveSentence(
                        createInductiveQuantifier(
                                createForAll(
                                        timeVar,
                                        createInductiveDefinitionConnector(
                                                createPredicate(propMap.get(prop), argList),
                                                createPredicate(
                                                        this.source.getCauseOf(
                                                                propMap.get(prop)
                                                        ), timeVar
                                                )
                                        )
                                )
                        )
                ));

                if(this.source.hasInitialProposition(prop)){
                    definitions.add(
                            createInductiveSentence(
                                    createInductivePredicateHead(
                                            createPredicate(
                                                    propMap.get(prop),
                                                    createInteger(0)
                                            )
                                    )
                            )
                    );
                }

                toReturn.addElement(createInductiveDefinition(definitions));
        }


		/**
		 * nodig: alle causations van elk fluent predicaat
		 * resultaat voor elk predicaat:
		 * {
		 *     !(var [Unfilled])*aantal argumenten keer* t [Time]: C_*pred*(t,*vars*) <- *causation*).
		 *     *dit voor elke causation van hetzelfde predicaat*
		 * }
		 */
		Map<FodotPredicateDeclaration, Set<FodotNextData>> nextMap = source.getNextMap();
		if (!nextMap.isEmpty()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("The fluent predicates' causations"));
		}
		for (FodotPredicateDeclaration predicate : nextMap.keySet()) {
			List<FodotInductiveSentence> definitions = new ArrayList<>();
			for (FodotNextData data : nextMap.get(predicate)) {
				definitions.add(createInductiveSentence(createInductiveDefinitionConnector(data.getPredicate(), data.getFormula())));
			}
			toReturn.addElement(createInductiveDefinition(definitions));
		}

		/**
		 * nodig: alle causations van elk static predicaat
		 * resultaat voor elk predicaat:
		 * {
		 *     !(var [Unfilled])*aantal argumenten keer*: *pred*(*vars*) <- *causation*).
		 *     *dit voor elke causation van hetzelfde predicaat*
		 * }
		 */
		Map<FodotPredicateDeclaration, Set<FodotCompoundData>> compoundMap = source.getCompoundMap();
		if (!compoundMap.isEmpty()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("The static predicates' causations"));
		}
		for (FodotPredicateDeclaration predicate : compoundMap.keySet()) {
			List<FodotInductiveSentence> definitions = new ArrayList<>();
			for (FodotCompoundData data : compoundMap.get(predicate)) {
				definitions.add(createInductiveSentence(createInductiveDefinitionConnector(data.getPredicate(), data.getFormula())));
			}
			if(this.source.getStaticValues().containsKey(predicate)) {
				for (IFodotPredicateEnumerationElement enumerationElement : this.source.getStaticValues().get(predicate)) {
					definitions.add(
							createInductiveSentence(
									createInductivePredicateHead(enumerationElement.toPredicate())
									)
							);
				}
			}
			toReturn.addElement(createInductiveDefinition(definitions));
		}

		/**
		 * nodig: alle legals, als legal head en legal body
		 * resultaat voor elk koppel:
		 * !(var [Unfilled])*aantal argumenten keer* t [Time]: *legal head* <- *legal body*
		 */
		if (! (source.getLegalMap().isEmpty() && source.getLegalSet().isEmpty()) ) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Translation of the LEGAL sentences"));
			List<FodotInductiveSentence> definitions = new ArrayList<>();
			for (Map.Entry<FodotPredicate, Set<IFodotFormula>> entry : source.getLegalMap().entrySet()) {
				for (IFodotFormula body : entry.getValue()) {
					if (body == null) {
						definitions.add(
								createInductiveSentence(
										createInductivePredicateHead(entry.getKey())
										)
								);
					} else {
						definitions.add(
								createInductiveSentence(
										createInductiveDefinitionConnector(entry.getKey(), body)
										)
								);
					}
				}
			}
			for (FodotPredicate predicate : source.getLegalSet()) {
				definitions.add(
						createInductiveSentence(
								createInductivePredicateHead(predicate)
								)
						);
			}
			toReturn.addElement(createInductiveDefinition(definitions));
		}

		/**
		 * nodig: elke goal, als *player*, *score*, *voorwaarden*
		 * resultaat:
		 * {
		 *    Score(*player*) = *score* <- (!t [Time]: terminalTime(t) => *voorwaarden*)
		 *    *en dit voor elk tripel*
		 * }
		 */
		Map<Pair<IFodotTerm, IFodotTerm>, Set<IFodotFormula>> scoreMap = source.getScoreMap();
		if (!scoreMap.isEmpty()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Translation of the SCORE sentences"));
		}
		List<FodotInductiveSentence> definitions = new ArrayList<>();
		for (Pair<IFodotTerm, IFodotTerm> scorePair: scoreMap.keySet()) {
			IFodotTerm playerTerm = scorePair.left;
			IFodotTerm score = scorePair.right;
			for (IFodotFormula formula : scoreMap.get(scorePair)) {

				FodotInductiveFunction scoreFunction = 
						createInductiveFunctionHead(
								createFunction(
										scoreFunctionDeclaration,
										playerTerm
										),
										score
								);

				//Check if no formula was specified: it is a general truth.
				// (because of EDGECASE: 'distinct_beginning_rule.kif')
				if (formula == null) {
					toReturn.addElement(createSentence(scoreFunction));
				} else {
					definitions.add(
							createInductiveSentence(createInductiveDefinitionConnector(
									scoreFunction, FormulaUtil.makeVariableFree(
											formula, scoreFunction.getFreeVariables())
									)
									)
							);
				}
			}
		}
		if (!definitions.isEmpty()) {
			toReturn.addElement(createInductiveDefinition(definitions));
		}

		/**
		 * nodig: elke terminal *voorwaarde*
		 * resultaat:
		 * {
		 *     !t [Time]: terminalTime(t) <- *voorwaarde*
		 *     *voor elke voorwaarde*
		 * }
		 */
		definitions = new ArrayList<>();
		if (!scoreMap.isEmpty()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Translation of the TERMINAL sentences"));
		}
		for (IFodotFormula formula : source.getTerminalSet()) {
			Set<FodotVariable> formulaVariables = new HashSet<FodotVariable>();
			FodotVariable timeVar = null;
			for (IFodotElement el : formula.getAllInnerElementsOfClass(FodotVariable.class)) {
				FodotVariable current = (FodotVariable) el;
				formulaVariables.add(current);
				if (current.getType().equals(source.getTimeType())) {
					timeVar = current;
				}
			}

			if (timeVar == null) {
				timeVar = createVariable("t", source.getTimeType(), formulaVariables);
			}

			FodotVariable earlierTime = createVariable("t2", source.getTimeType(),formulaVariables);
			definitions.add(
					createInductiveSentence(
							createInductiveDefinitionConnector(
									createPredicate(
											this.terminalTimePredicateDeclaration,
											timeVar
											),
											createAnd(
													formula,
													createNot(
															createExists(
																	earlierTime,
																	createAnd(
																			createLessThan(
																					earlierTime,
																					timeVar
																			),
																			createPredicate(
																					this.terminalTimePredicateDeclaration,
																					earlierTime
																			)
																	)
															)
													)
											)
									)
							)
					);
		}
		toReturn.addElement(createInductiveDefinition(definitions));

		return toReturn;
	}

	private FodotStructure buildStructure(FodotVocabulary voc) {
		FodotStructure toReturn = getDefaultStructure(voc);

		/**
		 * Data found by GdlVocabulary
		 */
		if (source.getGdlVocabulary().containsStructureElements()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Elements found by in first phase"));
			toReturn.addAllElements(source.getGdlVocabulary().getStructureElements());
		}


		/**
		 * nodig: Initiele *waarden* voor elk fluent *predicaat*
		 * resultaat voor elk predicaat:
		 * I_*predicaat* = {*waarden()*}
		 * OPGEPAST: in de structure moet achter elke constante een ()
		 */
		Map<FodotPredicateDeclaration, Set<IFodotPredicateEnumerationElement>> initMap
		= this.source.getInitialValues();
		if (!initMap.isEmpty()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("Initial values for the fluent predicates"));
		}
		for (FodotPredicateDeclaration declaration : initMap.keySet()) {
			toReturn.addElement(
					createPredicateEnumeration(this.source.getInitialOf(declaration),
							//createPredicateEnumeration(null,
							new ArrayList<>(initMap.get(declaration)))
					);
		}

		/**
		 * nodig: *waarden* voor elk statisch *predicaat*
		 * resultaat:
		 * *predicaat*={*waarden()*}
		 */
		Map<FodotPredicateDeclaration, Set<IFodotPredicateEnumerationElement>> staticMap
		= this.source.getStaticValues();
		if (!staticMap.isEmpty()) {
			toReturn.addElement(createBlankLines(1));
			toReturn.addElement(createComment("All values found in the static predicates"));
		}
		for (FodotPredicateDeclaration declaration : staticMap.keySet()) {
			if(!this.source.getCompoundMap().containsKey(declaration)) {
				toReturn.addElement(
						createPredicateEnumeration(declaration,
								new ArrayList<>(staticMap.get(declaration)))
						);
			}
		}

        // Voor elk dynamisch predicaat en proposition zonder causation in de map
        // moet zijn causation leeg zijn. Anders random gedrag.

        for (FodotPredicateDeclaration declaration :
                this.source.getGdlVocabulary().getDynamicPredicates()) {
            if(!this.source.getNextMap().containsKey(declaration) && !this.source.getCompoundMap().containsKey(declaration)){
                toReturn.addElement(
                        createPredicateEnumeration(
                                this.source.getCauseOf(declaration),
                                new ArrayList<IFodotPredicateEnumerationElement>()
                        )
                );
            }
        }

        for (FodotPredicateDeclaration declaration :
                this.source.getGdlVocabulary().getPropositions().values()) {
            if(!this.source.getNextMap().containsKey(declaration)){
                toReturn.addElement(
                        createPredicateEnumeration(
                                this.source.getCauseOf(declaration),
                                new ArrayList<IFodotPredicateEnumerationElement>()
                        )
                );
            }
        }

		return toReturn;
	}

	private static final String SCORE_TERM_NAME = "InversePlayerScore";

	private List<FodotTermDefinition> buildTerms(FodotVocabulary voc) {
		List<FodotTermDefinition> result = new ArrayList<>();

		/**
		 *	term PlayerScore : V {
		 * 		-Score(robot)
		 * 	}
		 */
		FodotFunction playerScore = createFunction(scoreFunctionDeclaration, source.getOwnRole());
		FodotTermDefinition scoreDefinition = createTermDefinition( SCORE_TERM_NAME, voc, createNegateInteger(playerScore) );
		result.add(scoreDefinition);
		
		return result;

	}


	private FodotProcedures buildProcedures() {
		return getDefaultProcedures();
	}

	/***************************************************************************
	 * Default fodot contents
	 **************************************************************************/

	private FodotVocabulary getDefaultVocabulary() {
		FodotLTCVocabulary defaultVoc = createLTCVocabulary();

		defaultVoc.addElement(createComment("Default vocabulary elements (and types needed by the default elements)"));

		// type Time isa nat
		defaultVoc.addElement(createTypeDeclaration(source.getTimeType()));

		// Start: Time
		defaultVoc.addElement(startFunctionDeclaration);

		// partial Next(Time):Time
		defaultVoc.addElement(nextFunctionDeclaration);

		// type Action constructed from {*action*}
		defaultVoc.addElement(createTypeDeclaration(source.getActionType()));

		// terminalTime(Time)
		defaultVoc.addElement(terminalTimePredicateDeclaration);

		// type ScoreType isa nat
		defaultVoc.addElement(scoreTypeDeclaration);

		// Score(Player): ScoreType
		defaultVoc.addElement(scoreFunctionDeclaration);

		// do(Time, Player, Action)
		defaultVoc.addElement(doPredicateDeclaration);

		// fodot_legal_move(Time, Player, Action)
		defaultVoc.addElement(this.source.getLegalmovePredicateDeclaration());

		return defaultVoc;
	}

	private FodotTheory getDefaultTheory(FodotVocabulary voc) {
		FodotTheory defaultTheory = createTheory(voc);

		defaultTheory.addElement(createComment("Default theory elements:"));

		Set<FodotVariable> variables = new HashSet<FodotVariable>();
		FodotVariable a_Action = createVariable("a", source.getActionType(), variables);
		variables.add(a_Action);
		FodotVariable p_Player = createVariable("p", source.getPlayerType(), variables);
		variables.add(p_Player);
		FodotVariable t_Time = createVariable("t", source.getTimeType(), variables);
		variables.add(t_Time);
		FodotVariable t2_Time = createVariable("t2",source.getTimeType(), variables);
		variables.add(t2_Time);

        /**
         * Old: !a [Action] p [Player] t [Time]: do(t,p,a) => ~terminalTime(t) & (?t2 [Time]: Next(t) = t2).
         *
         * New: ! a [Action] p [Player] t [Time] : do(t,p,a) => (! t2: terminalTime(t2) => t < t2).
         */
		variables = new HashSet<>(Arrays.asList(a_Action,p_Player,t_Time));
		defaultTheory.addElement(createSentence(createForAll(variables,
				createImplies(
						createPredicate(this.doPredicateDeclaration
								, new ArrayList<IFodotTerm>(Arrays.asList(t_Time,
                                        p_Player,
                                        a_Action))
                        ),
                        createForAll(t2_Time,
                                createImplies(
                                        createPredicate(this.terminalTimePredicateDeclaration,
                                                t2_Time),
                                        createLessThan(t_Time,t2_Time)
                                ))
						)
				)));

        /**
         * OLD: ! t [Time] p [Player]: ~terminalTime(t) & (?t2 [Time]: Next(t) = t2) => ?1 a [Action]: do(t,p,a).
         *
         * New: ! p [Player] t [Time] t2 [Time]: (terminalTime(t2) & t < t2) => (?=1 a [Action] : do(t,p,a)).
         *      ! p [Player] t [Time] t2 [Time]: (terminalTime(t2) & t >= t2) => (~? a [Action] : do(t,p,a)).
         */
        variables = new HashSet<>(Arrays.asList(t_Time,p_Player,t2_Time));
		defaultTheory.addElement(createSentence(createForAll(variables,
				createImplies(
						createAnd(
                                createPredicate(
                                        this.terminalTimePredicateDeclaration,
                                        t2_Time),
                                createLessThan(t_Time,
                                        t2_Time)
								), createExistsExactly(1, a_Action,
										createPredicate(this.doPredicateDeclaration,
												t_Time, p_Player, a_Action)
										)
						)
				)));
        defaultTheory.addElement(createSentence(createForAll(variables,
                createImplies(
                        createAnd(
                                createPredicate(
                                        this.terminalTimePredicateDeclaration,
                                        t2_Time),
                                createGreaterThanOrEqualTo(t_Time,
                                        t2_Time)
                        ), createNot(createExists(a_Action,
                                        createPredicate(this.doPredicateDeclaration,
                                                t_Time, p_Player, a_Action)
                                )
                        )
                )
        )));

        /**
         * OLD:
         * {
         *    !t: Next(t) = t+1 <- ~terminalTime(t) & (?t2 [Time]: Next(t2)=t).
         *    Next(0) = 1.
         * }
         *
         * New:
         * {
         *    ! t [Time] : Next(t) = t + 1 <- Time(t+1).
         * }
         */
        List<FodotInductiveSentence> definitions = new ArrayList<>();
        definitions.add(
                createInductiveSentence(
                        createInductiveQuantifier(
                                createForAll(t_Time,
                                        createInductiveDefinitionConnector(
                                                createInductiveFunctionHead(
                                                        createFunction(this.nextFunctionDeclaration, t_Time),
                                                        createAddition(t_Time, createInteger(1))
                                                ), createPredicate(
                                                        this.source.getTimeType().getTypePredicateDeclaration(),
                                                        createAddition(
                                                                t_Time,
                                                                createInteger(1)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        defaultTheory.addElement(createInductiveDefinition(definitions));

		// !t p a: does(t,p,a) => legalmove(t,p,a).

		defaultTheory.addElement(
				createSentence(
						createImplies(
								createPredicate(
										this.doPredicateDeclaration,
										t_Time,
										p_Player,
										a_Action
										),
										createPredicate(
												this.source.getLegalmovePredicateDeclaration(),
												t_Time,
												p_Player,
												a_Action
												)
								)
						)
				);

		return defaultTheory;
	}

	private FodotStructure getDefaultStructure(FodotVocabulary voc) {
		FodotStructure defaultStructure = createStructure(voc);

		defaultStructure.addElement(createComment("Default structure elements:"));
		//Start=0
		defaultStructure.addElement(
				createConstantFunctionEnumeration(
						this.startFunctionDeclaration,
						createInteger(0).toEnumerationElement()
						)
				);

		//Time={0..Constant(timeLimit)}
		defaultStructure.addElement(
				createNumericalTypeRangeEnumeration(
						source.getTimeType(),
						createInteger(0),
						createInteger(this.turnLimit)
						)
				);

		return defaultStructure;
	}

	public static final int DEFAULT_IDP_TIME_LIMIT = 100;
	private int idpTimeLimit = DEFAULT_IDP_TIME_LIMIT;
	private static final int DEFAULT_MODEL_LIMIT = 1;
	private int idpModelLimit = DEFAULT_MODEL_LIMIT;

	private FodotProcedures getDefaultProcedures() {
		//stdoptions.timeout=timeLimit
		//stdoptions.nbmodels=5
		//printmodels(modelexpand(T,S))
		List<FodotProcedureStatement> proc = new ArrayList<>(
				Arrays.asList(
						createProcedure("stdoptions.timeout="+idpTimeLimit),
						createProcedure("stdoptions.nbmodels="+idpModelLimit),
						createProcedure("stdoptions.cpsupport = true"),
						createProcedure("printmodels(minimize(T,S,"+SCORE_TERM_NAME+"))")
						)
				);

		return createProcedures("main",proc);
	}

}
