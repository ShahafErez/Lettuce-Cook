export default function Diets(props) {
  let { vegetarian, vegan, glutenFree, dairyFree } = props;
  let symbolSize = props.symbolSize;

  return (
    <div className="diet">
      {vegetarian && (
        <img
          src="/images/vegetarian.png"
          alt="vegetarian"
          title="vegetarian"
          height={symbolSize}
        />
      )}
      {vegan && (
        <img
          src="/images/vegan.png"
          alt="vegan"
          title="vegan"
          height={symbolSize}
        />
      )}
      {glutenFree && (
        <img
          src="/images/gluten-free.png"
          alt="gluten-free"
          title="gluten free"
          height={symbolSize}
        />
      )}
      {dairyFree && (
        <img
          src="/images/dairy-free.png"
          alt="dairy-free"
          title="dairy free"
          height={symbolSize}
        />
      )}
    </div>
  );
}
