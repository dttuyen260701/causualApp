export interface ICardFront{
	title: string
	image: string
}

export interface ICardBack{
	title: string
	description: string
}

export interface ICard{
	cardFront: ICardFront
	cardBack: ICardBack
}